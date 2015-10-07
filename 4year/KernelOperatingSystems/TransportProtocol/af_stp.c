/*
 * DUMITRESCU EVELINA 341C3
 * Tema 5 SO2 Transport Protocol
 */

#include "stp.h"

MODULE_DESCRIPTION("SO2 Transport Protocol");
MODULE_AUTHOR("Eu");
MODULE_LICENSE("GPL");

struct stats {
	int rx_pkts, hdr_err, csum_err, no_sock, no_buffs, tx_pkts;
};

struct stats stp_stats;
struct proc_dir_entry *p_entry;
static struct list_head stp_bind_list;


/* function prototipes */
static int stp_release(struct socket *sock);
static int stp_rcv(struct sk_buff *skb, struct net_device *dev,
		   struct packet_type *pt, struct net_device *orig_dev);
static int stp_connect(struct socket *sock, struct sockaddr *addr, int len,
		       int flags);
static int stp_bind(struct socket *sock, struct sockaddr *addr, int addr_len);
static int stp_sendmsg(struct kiocb *iocb, struct socket *sock,
		       struct msghdr *msgh, size_t len);
static int stp_recvmsg(struct kiocb *iocb, struct socket *sock,
		       struct msghdr *msgh, size_t len, int flags);
static int stp_sock_create(struct net *net, struct socket *sock, int proto,
			   int kern);
static int stp_seq_open(struct inode *inode, struct file *file);

static const struct proto_ops stp_proto_ops = {
	.family = AF_STP,
	.connect = stp_connect,
	.release = stp_release,
	.bind = stp_bind,
	.sendmsg = stp_sendmsg,
	.recvmsg = stp_recvmsg,
	.poll = datagram_poll,
	.socketpair = sock_no_socketpair,
	.accept = sock_no_accept,
	.listen = sock_no_listen,
	.shutdown = sock_no_shutdown,
	.setsockopt = sock_no_setsockopt,
	.getsockopt = sock_no_getsockopt,
	.mmap = sock_no_mmap,
	.sendpage = sock_no_sendpage,
};

static struct proto stp_proto = {
	.owner = THIS_MODULE,
	.obj_size = sizeof(struct stp_sock),
	.name = STP_PROTO_NAME,
};

static const struct file_operations stp_file_ops = {
	.owner = THIS_MODULE,
	.release = single_release,
	.open = stp_seq_open,
	.read = seq_read,
	.llseek = seq_lseek,
};

static const struct net_proto_family stp_net_proto_family = {
	.owner = THIS_MODULE,
	.family = AF_STP,
	.create = stp_sock_create,
};


static struct stp_sock *stp_sk(struct sock *sk)
{
	return (struct stp_sock *)sk;
}


/* bind list functions */
static void stp_info_add_to_list(int iface, __be16 port, struct sock *sk)
{
	struct stp_info *si;
	si = kmalloc(sizeof(*si), GFP_KERNEL);
	if (si == NULL)
		return;
	si->iface = iface;
	si->port_local = port;
	list_add(&si->list, &stp_bind_list);

}

static struct stp_info *stp_info_find_port(__be16 port)
{
	struct list_head *p;
	struct stp_info *si;

	list_for_each(p, &stp_bind_list) {
		si = list_entry(p, struct stp_info, list);
		if (si->port_local == port)
			return si;
	}
	return NULL;
}

static void stp_info_purge_list(void)
{
	struct list_head *p, *q;
	struct stp_info *si;

	list_for_each_safe(p, q, &stp_bind_list) {
		si = list_entry(p, struct stp_info, list);
		list_del(p);
		kfree(si);
	}
}

/* register packet type proto hook */
static void register_prot_hook(struct sock *sk)
{
	struct stp_sock *ssk = stp_sk(sk);
	dev_add_pack(&ssk->prot_hook);
}

/* unregister packet type proto hook */
static void unregister_prot_hook(struct sock *sk)
{
	struct stp_sock *ssk = stp_sk(sk);
	dev_remove_pack(&ssk->prot_hook);
}


/* create STP socket */
static int stp_sock_create(struct net *net, struct socket *sock, int proto,
			   int kern)
{
	struct sock *sk;
	int err = 0;
	if (!sock || proto != 0) {
		err = -EINVAL;
		goto out;
	}
	if (sock->type != SOCK_DGRAM) {
		err = -ESOCKTNOSUPPORT;
		goto out;
	}

	sock->state = SS_UNCONNECTED;
	sk = sk_alloc(net, PF_STP, GFP_ATOMIC, &stp_proto);
	if (sk == NULL) {
		err = -ENOBUFS;
		goto out;
	}

	sock->ops = &stp_proto_ops;
	sock_init_data(sock, sk);
	sk->sk_family = AF_STP;
	sk->sk_protocol = (__force __be16) proto;

 out:
	return err;
}


/* release STP socket */
static int stp_release(struct socket *sock)
{
	struct sock *sk;
	if (!sock)
		return -EINVAL;
	sk = sock->sk;
	if (!sk)
		return -EINVAL;

	unregister_prot_hook(sk);
	sock_put(sock->sk);
	sock->sk = NULL;
	return 0;
}


/* bind STP socket to address */
static int stp_bind(struct socket *sock, struct sockaddr *addr, int addr_len)
{

	int err = 0;
	struct sockaddr_stp *stp_addr;
	struct sock *sk;
	struct stp_sock *stp;
	__be16 snum;
	if (!addr || !sock) {
		err = -EINVAL;
		goto out;
	}

	if (addr_len < sizeof(struct sockaddr_stp)) {
		err = -EINVAL;
		goto out;
	}

	stp_addr = (struct sockaddr_stp *)addr;
	if (stp_addr->sas_family != AF_STP) {
		err = -EAFNOSUPPORT;
		goto out;
	}

	sk = sock->sk;
	stp = (struct stp_sock *)sk;

	snum = ntohs(stp_addr->sas_port);
	if (snum == 0 || snum > 65535)
		goto out;

	if (stp_info_find_port(stp_addr->sas_port)) {
		err = -EINVAL;
		goto out;
	}

	stp->port_local = stp_addr->sas_port;
	stp->iface = stp_addr->sas_ifindex;
	stp_info_add_to_list(stp_addr->sas_ifindex, stp_addr->sas_port, sk);
	stp->prot_hook.func = stp_rcv;
	stp->prot_hook.type = htons(ETH_P_ALL);
	stp->prot_hook.af_packet_priv = sk;
	stp->prot_hook.dev = NULL;
	register_prot_hook(&stp->sk);
 out:
	return err;

}


/* create new connection for STP protocol */
static int stp_connect(struct socket *sock, struct sockaddr *addr, int len,
		       int flags)
{
	int err = 0;
	struct stp_sock *ssk;
	struct sockaddr_stp *stp_addr = (struct sockaddr_stp *)addr;
	if (!sock || !addr) {
		err = -EINVAL;
		goto out;
	}

	stp_addr = (struct sockaddr_stp *)addr;
	if (sizeof(*stp_addr) > len) {
		err = -EINVAL;
		goto out;
	}

	if (!sock->sk) {
		err = -EINVAL;
		goto out;
	}

	ssk = (struct stp_sock *)sock->sk;
	ssk->port_rem = stp_addr->sas_port;
	memcpy(ssk->hwaddr, stp_addr->sas_addr, 6 * sizeof(__u8));

 out:
	return err;

}


/* packet type proto_hook function
 * adds skbs to socket queue
 */
static int stp_rcv(struct sk_buff *skb, struct net_device *dev,
		   struct packet_type *pt, struct net_device *orig_dev)
{

	int err = 0;
	struct stp_hdr *stph;
	struct sock *sk;
	__be16 dport, sport;
	struct stp_sock *ssk;

	stph = (struct stp_hdr *)skb->data;
	dport = stph->dst;
	sport = stph->src;

	if (!skb || !dev) {
		err = -EINVAL;
		goto out;
	}
	sk = pt->af_packet_priv;
	ssk = (struct stp_sock *)sk;

	return 0;
	if (ssk->port_rem == dport && ssk->port_local == sport)
		err = sock_queue_rcv_skb(sk, skb);

 out:
	return err;

}


/* STP socket receive message method */
static int stp_recvmsg(struct kiocb *iocb, struct socket *sock,
		       struct msghdr *msgh, size_t len, int flags)
{

	int err = 0;
	struct sk_buff *skb;
	unsigned int stphlen = sizeof(struct stp_hdr);
	struct sock *sk;
	struct stp_sock *ssk;
	ssk = (struct stp_sock *)sock->sk;

	if (flags & MSG_ERRQUEUE)
		return -EINVAL;
	sk = sock->sk;
	if (!sock)
		return -EINVAL;
	if (!msgh)
		return -EINVAL;
	if (!sk)
		return -EINVAL;

	stp_stats.rx_pkts++;
	return len;

	/*FIXME here fails */
	skb =
	    skb_recv_datagram(sk, flags & ~MSG_DONTWAIT, flags & MSG_DONTWAIT,
			      &err);
	if (!skb)
		return err;
	err = skb_copy_datagram_iovec(skb, stphlen, msgh->msg_iov, len);
	if (err)
		return err;

	sock_recv_ts_and_drops(msgh, sk, skb);
	if (skb)
		skb_free_datagram(sk, skb);

	return len;

}


/* build  header STP protocol */
static int stp_build_header(struct socket *sock, struct msghdr *msgh,
			    struct stp_hdr *stp_header, size_t len)
{

	struct stp_sock *ssk;
	struct sockaddr_stp *stp_addr;
	int err = 0;

	if (!sock || !msgh) {
		err = -EINVAL;
		goto out;
	}

	ssk = (struct stp_sock *)sock->sk;

	stp_header->flags = 0;
	stp_header->csum = 0;
	stp_header->src = ssk->port_local;
	stp_header->len = sizeof(stp_header) + len;
	if (msgh->msg_name) {
		stp_addr = (struct sockaddr_stp *)(msgh->msg_name);
		if (stp_addr->sas_port == 0) {
			err = -EINVAL;
			goto out;
		}
		stp_header->dst = stp_addr->sas_port;
		ssk->port_rem = stp_addr->sas_port;
	} else {
		if (ssk->port_rem == 0) {
			err = -EINVAL;
			goto out;
		}
		stp_header->dst = ssk->port_rem;

	}

 out:

	return err;

}


/* builds an skb that encapsulates a STP protocol */
static int stp_fill_skb(struct sk_buff *skb, struct net_device *dev,
			struct socket *sock, struct iovec *msg_iov,
			struct stp_hdr stp_header, size_t len)
{
	int err = 0;
	unsigned char *data1;
	unsigned char *data2;
	size_t stp_header_len;
	__u8 *lo_hwaddr = kmalloc(6 * sizeof(__u8), GFP_KERNEL);
	memset(lo_hwaddr, 0, 6 * sizeof(__u8));
	stp_header_len = sizeof(stp_header);

	/* reserve enough spaqce for ethernet & stp header & data */
	skb_reserve(skb, 14 + stp_header_len);

	/* adds data payload */
	data2 = skb_put(skb, len);
	/* copies message payload from msg_iov structure to skb */
	err = skb_copy_datagram_from_iovec(skb, 0, msg_iov, 0, len);
	/* inserts stp header */
	data1 = skb_push(skb, stp_header_len);
	memcpy(data1, &stp_header, stp_header_len);

	if (err == -EFAULT)
		goto out;

	stp_header.csum = 0;
	/* inserts ethernet header */
	err =
	    dev_hard_header(skb, dev, ntohs((ETH_P_STP)), lo_hwaddr, NULL,
			    len + stp_header_len);
	if (err < 0)
		goto out;

	skb->dev = dev;
	skb->sk = sock->sk;
	skb->protocol = htons(ETH_P_STP);
	skb->priority = sock->sk->sk_priority;
 out:
	kfree(lo_hwaddr);
	return err;
}


/* STP socket send message method */
static int stp_sendmsg(struct kiocb *iocb, struct socket *sock,
		       struct msghdr *msgh, size_t len)
{

	int err = 0;

	struct stp_hdr stp_header;
	struct sk_buff *skb;
	struct net_device *dev = NULL;
	size_t stp_header_len = sizeof(stp_header);

	/* increment number of send packets*/
	stp_stats.tx_pkts++;
	/* build stp header*/
	err = stp_build_header(sock, msgh, &stp_header, len);

	if (err != 0)
		goto out;
	/* get networking device by iface index */
	dev = dev_get_by_index_rcu(sock_net(sock->sk), stp_sk(sock->sk)->iface);

	if (dev == NULL) {
		err = -EINVAL;
		goto out;
	}

	/* memory allocation for skb */
	skb =
	    sock_alloc_send_skb(sock->sk,
				stp_header_len + 14 + len +
				dev->needed_tailroom, 0, &err);
	if (!skb) {

		err = -ENOMEM;
		goto out;
	}

	/* build the skb*/
	err = stp_fill_skb(skb, dev, sock, msgh->msg_iov, stp_header, len);
	if (err < 0)
		goto out;

	/* send the skb */
	err = dev_queue_xmit(skb);
	if (err > 0) {
		err = net_xmit_errno(err);
		if (err != 0)
			goto out;
	}
	return len;

 out:

	if (dev != NULL)
		dev_put(dev);


	return err;
}

/* proc interface output method*/
static int my_seq_show(struct seq_file *m, void *v)
{
	seq_printf(m,
		   "RxPkts HdrErr CsumErr NoSock NoBuffs TxPkts\n%d %d %d %d %d %d\n",
		   stp_stats.rx_pkts, stp_stats.hdr_err, stp_stats.csum_err,
		   stp_stats.no_sock, stp_stats.no_buffs, stp_stats.tx_pkts);
	return 0;
}

static int stp_seq_open(struct inode *inode, struct file *file)
{
	return single_open(file, my_seq_show, NULL);
}


/* called when module init */
int __init stp_init(void)
{
	int err = 0;
	p_entry =
	    proc_create(STP_PROC_NET_FILENAME, 0, init_net.proc_net,
			&stp_file_ops);
	if (!p_entry)	{
		err = -ENOMEM;
		goto out_proc;
	}
	sock_register(&stp_net_proto_family);
	err = proto_register(&stp_proto, 0);
	if(err) {
		goto out_protreg;
	}
	INIT_LIST_HEAD(&stp_bind_list);

out_protreg:
		sock_unregister(AF_STP);
out_proc:
	return err;
}

/* called when module removed */
void __exit stp_exit(void)
{
	remove_proc_entry(STP_PROC_NET_FILENAME, init_net.proc_net);
	sock_unregister(AF_STP);
	proto_unregister(&stp_proto);
	stp_info_purge_list();
}

module_init(stp_init);
module_exit(stp_exit);
