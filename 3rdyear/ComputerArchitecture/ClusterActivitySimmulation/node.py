"""
    This module represents a cluster's computational node.

    Computer Systems Architecture course
    Assignment 1 - Cluster Activity Simulation
    march 2013
    
    DUMITRESCU EVELINA 331CA 
    
"""
import threading
import thread
from threading import *

#the stack size to be used for subsequently created threads
threading.stack_size(32*1024)

'''
class that stores the node response for a specific query
'''
class NodeResponse:
    def __init__(self, row, col, matrix_type, element):
        """
            Constructor.

            @param row: the row position of the element inside the matrix
            @param col: the column position of the element inside the matrix
            @param matrix_type: the type of the matrix 
            type 'a' is for matrix A and type 'b' is for matrix B
            @param element: the requested element
        """
        self.row=row
        self.col=col
        self.matrix_type=matrix_type
        self.element=element        
        

'''
class that stores a given task for a node    
'''
class Task:
    def __init__(self, calling_node, row,col, matrix_type):
        """
            Constructor.
            @param calling_node: the node that has given the task and expects
            the answer back
            @param row: the row position of the element inside the matrix
            @param col: the column position of the element inside the matrix
            @param matrix_type: the type of the matrix 
            type 'a' is for matrix A and type 'b' is for matrix B
        """

        self.calling_node = calling_node
        self.row=row
        self.col=col
        self.matrix_type=matrix_type
        self.event=Event()
        self.event.clear()

'''
    class used for computing a matrix multiplication task
'''         
class ComputeMatrix():
    def __init__( self, node, start_row,  no_rows,start_col, no_cols,block_size, matrix_size, dict_nodes):
        """
            Constructor.
            @param node: the node that processes the task
            @param start_row: the upper left corner row of the matrix block
            @param start_col: the upper left corner column of the matrix block
            @param no_row: the number of rows of the block
            @param no_col: the number of columns of the block
            @param block_size: the block size of each node datastore
            @param matrix_size: the size of the matrix
            @param dict_nodes: a dictionary containing all the nodes
            key = (node_ID)
            value = the node  
        """

        self.node = node
        self.dict_nodes=dict_nodes
        self.start_row=start_row
        self.start_col=start_col
        self.no_rows=no_rows
        self.no_cols=no_cols
        self.matrix_size=matrix_size
        self.block_size=block_size
        # used to store the task queries given to the nodes
        self.queries_to_nodes=[]
        #used to store the responses from the queried node
        self.responses_from_nodes=[]
        # lock 
        self.lock=Lock()
        #result: the required block from the matrix multiplication
        self.result=[[0 for i in range(self.no_cols)] for j in range(self.no_rows)]
        #rows required for matrix multiplication from the first matrix
        self.matrix_A=[[0 for i in range(self.matrix_size)] for j in range(self.matrix_size)]
        #columns required for matrix multiplication from the second matrix
        self.matrix_B=[[0 for i in range(self.matrix_size)] for j in range(self.matrix_size)]
        
    def receive_response(self,response):
        '''
           stores a response from a node 
        '''    
        self.lock.acquire()
        self.responses_from_nodes.append(response) 
        self.lock.release()
           
    def compute( self ):
        '''
            computes the block multiplication
        '''
        #counts the queried tasks
        num_tasks=0
        #asks other nodes for the elements stored in their datastores for matrix A
        #for an element m[i][j] only ask for the rows i from A
        for i in range(self.no_rows):
            for j in range(self.matrix_size):
                #computes the node ID
                node_ID_i = (i+self.start_row)/self.block_size
                node_ID_j = (j)/self.block_size
                task=Task(self,i+self.start_row, j,'a')
                self.queries_to_nodes.append(task)              
                self.dict_nodes[(node_ID_i,node_ID_j)].watch_thread.receive_task(task)                
                num_tasks+=1       
        #asks other nodes for the elements stored in their datastores for matrix B                        
        #for an element m[i][j] only ask for the columns j from B        
        for i in range(self.matrix_size):
            for j in range(self.no_cols):
                #computes the node ID            
                node_ID_i = (i)/self.block_size
                node_ID_j = (j+self.start_col)/self.block_size                  
                task=Task(self,i, j+self.start_col,'b')
                self.queries_to_nodes.append(task)
                self.dict_nodes[(node_ID_i,node_ID_j)].watch_thread.receive_task(task)
                num_tasks+=1
        #waits until the number of queried tasks equals to the responses received
        while len(self.responses_from_nodes)!=len(self.queries_to_nodes):
                pass
        #builds the blocks needed for the multiplication 
        for ind in range(num_tasks):
            row=self.responses_from_nodes[ind].row
            col=self.responses_from_nodes[ind].col
            if(self.responses_from_nodes[ind].matrix_type == 'a'):
                    self.matrix_A[row][col]=self.responses_from_nodes[ind].element   
            else:
                    self.matrix_B[row][col]=self.responses_from_nodes[ind].element
        #multiply the 2 blocks
        for row in range(self.no_rows):
            for col in range(self.no_cols): 
                for k in range(0, self.matrix_size, 1):
                    self.result[row][col]+=self.matrix_A[row+self.start_row][k]*self.matrix_B[k][col+self.start_col] 
        
                    
'''
    class that represents a thread used for inter-node communication
''' 
class WatchThread(Thread):
    def __init__( self,node, block_size):
        """
            Constructor.
            @param node: the node that owns the thread
            @param block_size: the block size of the datastore
        """
        Thread.__init__( self )    
        self.node = node
        self.block_size=block_size
        #variable used to tell the thread to stop
        self.end=0
        #task counter
        self.count=0
        self.lock=Lock()
        #stores the given tasks; a list of Tasks objects
        self.data_tasks=[]
     
     
    def receive_task(self, task):
        """
            inserts a new task
        """
        self.lock.acquire()
        #increment tasks counter
        self.count+=1
        self.data_tasks.append(task)
        self.lock.release()
        
    def run( self ):
        '''
            represents the thread's activity
        '''    
        #registers the thread into the datastore
        self.node.data_store.register_thread(self.node)
        while self.end == 0:
             self.lock.acquire()
             #extract a new task
             if len(self.data_tasks)>0 :
                task = self.data_tasks.pop()
                row=task.row
                col=task.col
                #get the element
                if task.matrix_type == 'a':
                            elem= self.node.data_store.get_element_from_a(self.node,row%self.block_size,col%self.block_size)
                if task.matrix_type == 'b':
                            elem= self.node.data_store.get_element_from_b(self.node,row%self.block_size,col%self.block_size)    
                #sends the response to the node that has given the task
                node_response=NodeResponse(row,col,task.matrix_type,elem) 
                task.calling_node.receive_response(node_response)  
             self.lock.release()

                
class Node:
    """
        Class that represents a cluster node with computation and storage functionalities.
    """

    def __init__(self, node_ID, block_size, matrix_size, data_store):
        """
            Constructor.

            @param node_ID: a pair of IDs uniquely identifying the node; 
            IDs are integers between 0 and matrix_size/block_size
            @param block_size: the size of the matrix blocks stored in this node's datastore
            @param matrix_size: the size of the matrix
            @param data_store: reference to the node's local data store
        """
        self.node_ID=node_ID
        self.block_size = block_size
        self.matrix_size=matrix_size
        self.data_store=data_store
        #list of the tasks
        self.tasks=[]
        #nodes organized in a dictionary
        self.dict_nodes={}
        self.nodes=[]
        # the inter_node communication thread        
        self.watch_thread = WatchThread(self, block_size)
        #start the thread
        self.watch_thread.start()


    def set_nodes(self, nodes):
        """
            Informs the current node of the other nodes in the cluster. 
            Guaranteed to be called before the first call to compute_matrix_block.

            @param nodes: a list containing all the nodes in the cluster
        """
        self.nodes=nodes
        #organize the nodes ina  dictionary for easier manipulation
        for i in range(len(nodes)):
            node_ID_i=nodes[i].node_ID[0]
            node_ID_j=nodes[i].node_ID[1]
            self.dict_nodes[(node_ID_i, node_ID_j)]=nodes[i]
     

    def compute_matrix_block(self, start_row, start_column, num_rows, num_columns):
        """
            Computes a given block of the result matrix.
            The method invoked by FEP nodes.

            @param start_row: the index of the first row in the block
            @param start_column: the index of the first column in the block
            @param num_rows: number of rows in the block
            @param num_columns: number of columns in the block

            @return: the block of the result matrix encoded as a row-order list of lists of integers
        """
       
        compute_matrix=ComputeMatrix(self,start_row,num_rows,start_column,num_columns,self.block_size,self.matrix_size, self.dict_nodes)
        compute_matrix.compute()
        return compute_matrix.result
        
            
    def shutdown(self):
        """
            Instructs the node to shutdown (terminate all threads).
        """
        #tell the inter-node communication thread to shutdown
        self.watch_thread.end=1
        self.watch_thread.join()

