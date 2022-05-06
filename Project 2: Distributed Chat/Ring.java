
public class Ring {

	Node head;
	
	public Ring() {
		
		head = null;
		
	}
	
	 public Ring add(ThreadPC peer)
	 {
	        Node new_node = new Node(peer);
	        new_node.next = head;
	        
	        if (head == null) {
	        
	        	head = new_node;
	        	head.next = head;
	        	
	        }
	        
	        else {
	            
	        	Node current = head;
	        	
	            while (current.next != head) {
	            
	            	current = current.next;
	            
	            }
	  
	            current.next = new_node;

	        }
	   
	        return this;
	    }
	 
	 public void broadcastMsg(String message) {
		 
		 Node current = head;
		 
		 if(head == null)
			 return;
		 
		 while(current.next != head)
		 {
			 current.peer.getPrintWriter().println(message);
			 current = current.next;
		 
		 }
		 
		 current.peer.getPrintWriter().println(message);
		 
		 
	 }
	 
	 public void remove(ThreadPC peer) {
		 
		 Node current = head;
		 
		 if(head == null)
			 return;
		 
		 if(head.peer == peer) {
			 
			 head = head.next;
			 
		 }
		 
		 while(current.next != head)
		 {
			 if(current.next.peer == peer)
				 current.next = current.next.next;
			 
			 current = current.next;
		 
		 }
		 
	 }
	
}
