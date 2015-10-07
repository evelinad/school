#!/bin/sh

testexec()
{
   if [ $1 -eq 0 ];then
    echo "OK" 
   else
    echo "FAILED" 
   fi
   echo
     
}

authorization_server()
{

CSR="CA/ca.csr"
CA="CA/ca.pem"
CAKey="CA/ca.key"
CASerial="CA/ca.srl"
ASAlias="authorization_server_private"
ASKs="authorization/security/authorization_server.ks"
ASPass="authorization_server_password"
ASCSR="authorization/security/authorization_server.csr"
ASCert="authorization/security/authorization_server.crt"
CAAlias="certification_authority"

rm -fr CA/*

rm -fr authorization/security/*
rmdir authorization/security
mkdir authorization/security

rm -fr server/security/*
rmdir server/security
mkdir server/security

rm -fr client/security/*
rmdir client/security
mkdir client/security

echo  "Creating a private key and a certificate request for the CA"
openssl req -new -newkey rsa:1024 -nodes -out ${CSR} -keyout ${CAKey} -batch
testexec $?

echo  "Creating the self signed certificate of the CA"
openssl x509 -trustout -signkey ${CAKey} -days 365 -req -in ${CSR} -out ${CA}
testexec $?

echo  "Creating the file for the serial number of the CA"
echo "02" > ${CASerial}
testexec $?

echo  "Creating the keystore for the authorization server"
keytool -genkey -alias ${ASAlias} -keystore ${ASKs} -storetype JKS -keyalg rsa -dname "CN=authorization_server, OU=security, O=SPRC, L=Bucharest, S=Bucharest, C=RO" -storepass ${ASPass} -keypass ${ASPass}
testexec $?

echo "Creating a certificate request for the authorization server"
keytool -certreq -keyalg rsa -alias ${ASAlias} -keypass ${ASPass} -keystore ${ASKs} -storepass ${ASPass} -file ${ASCSR}
testexec $?

echo "Asking the CA to sign the certificate request of the authorization server"
openssl x509 -CA ${CA} -CAkey ${CAKey} -CAserial ${CASerial} -req -in ${ASCSR} -out ${ASCert} -days 365
testexec $?

echo "Importing the certificate of the CA into the authorization server keystore"
keytool -import -alias ${CAAlias} -keystore ${ASKs} -storepass ${ASPass} -trustcacerts -file ${CA}
testexec $?

echo "Importing the signed certificate of the authorization server into own keystore"
keytool -import -alias ${ASAlias} -keypass ${ASPass} -keystore ${ASKs} -storepass ${ASPass} -trustcacerts -file ${ASCert}
testexec $?

}



server()
{
rm -rf server/security/$1
mkdir server/security/$1

CACert="CA/ca.pem"
CAKey="CA/ca.key"
CASerial="CA/ca.srl"
CAAlias="certification_authority"

ASCertificate="authorization/security/authorization_server.crt"
ASAlias="authorization_server"

DSAlias=$1"_private"
DSKs=server/security/$1/$1".ks"
DSPass=$1"_password"
DSCSR=server/security/$1/$1".csr"
DSCert=server/security/$1/$1".crt"

echo "Creating the keystore for the  department server"
keytool -genkey -alias ${DSAlias} -keystore ${DSKs} -storetype JKS -keyalg rsa -dname "CN=$1, OU=department, O=SPRC, L=Bucharest, S=Bucharest, C=RO" -storepass ${DSPass} -keypass ${DSPass}
testexec $?

echo "Creating a certificate request for the  department server"
keytool -certreq -keyalg rsa -alias ${DSAlias} -keypass ${DSPass} -keystore ${DSKs} -storepass ${DSPass} -file ${DSCSR}
testexec $?

echo "Ask the CA to sign the certificate request of the  department server"
openssl x509 -CA ${CACert} -CAkey ${CAKey} -CAserial ${CASerial} -req -in ${DSCSR} -out ${DSCert} -days 365
testexec $?

echo "Importing the certificate of the CA into the department server's keystore"
keytool -import -alias ${CAAlias} -keystore ${DSKs} -storepass ${DSPass} -trustcacerts -file ${CACert}
testexec $?

echo "Importing the signed certificate of the department server into own keystore"
keytool -import -alias ${DSAlias} -keypass ${DSPass} -keystore ${DSKs} -storepass ${DSPass} -trustcacerts -file ${DSCert}
testexec $?

echo "Importing the signed certificate of the authorization server into the  department server keystore"
keytool -import -alias ${ASAlias} -keypass ${DSPass} -keystore ${DSKs} -storepass ${DSPass} -trustcacerts -file ${ASCertificate}
testexec $?

}

client()
{
rm -rf client/security/$1
mkdir client/security/$1

CACert="CA/ca.pem"
CAKey="CA/ca.key"
CASerial="CA/ca.srl"
CAAlias="certification_authority"
ASCert="authorization/security/authorization_server.crt"
ASAlias="authorization_server"
CAlias=$1"_private"
CKs=client/security/$1/$1".ks"
CPass=$1"_password"
CCert_request=client/security/$1/$1".csr"
CCert=client/security/$1/$1".crt"

echo "Creating the keystore for the client"
keytool -genkey -alias ${CAlias} -keystore ${CKs} -storetype JKS -keyalg rsa -dname "CN=$1, OU=$2, O=SPRC, L=Bucharest, S=Bucharest, C=RO" -storepass ${CPass} -keypass ${CPass}
testexec $?

echo "Creating a certificate request for the client'"
keytool -certreq -keyalg rsa -alias ${CAlias} -keypass ${CPass} -keystore ${CKs} -storepass ${CPass} -file ${CCert_request}
testexec $?

echo "Ask the CA to sign the certificate request of the client"
openssl x509 -CA ${CACert} -CAkey ${CAKey} -CAserial ${CASerial} -req -in ${CCert_request} -out ${CCert} -days 365
testexec $?

echo "Importing the certificate of the CA into the client keystore"
keytool -import -alias ${CAAlias} -keystore ${CKs} -storepass ${CPass} -trustcacerts -file ${CACert}
testexec $?

echo "Importing the signed certificate of the client into own keystore"
keytool -import -alias ${CAlias} -keypass ${CPass} -keystore ${CKs} -storepass ${CPass} -trustcacerts -file ${CCert}
testexec $?

echo "Importing the signed certificate of the authorization server into the  client keystore"
keytool -import -alias ${ASAlias} -keypass ${CPass} -keystore ${CKs} -storepass ${CPass} -trustcacerts -file ${ASCert}
testexec $?

}
case $1 in
    client )
       client $2 $3 ;;
    server )
      server $2  ;;
    auth )
       authorization_server ;;
esac
