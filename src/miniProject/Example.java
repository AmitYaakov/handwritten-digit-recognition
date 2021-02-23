package miniProject;

public class Example {
 
   private int label;
   private int[][] image;
   
   
   
   public Example(int label, int[][] image) {
		this.label = label;
		this.image = image;
   }
   public int getLabel() {
 		return label;
 	}
 	public void setLabel(int value) {
 		this.label = value;
 	}
 	public int[][] getImage() {
 		return image;
 	}
 	public void setImage(int[][] image) {
 		this.image = image;
 	}
 	
 	public boolean comparePixels(int [][]a, int [][]b) {
 		for (int i=0; i< a.length; i++){
 			for (int j=0; j<a[i].length;j++) {
 				if (a[i][j] != b[i][j]) {
 					return false;
 				}
 			}
 		}
 		return true; 
 	}
 	
 	public boolean equals(Object o) { 

        if (o == this) { 
            return true;
        } 
        if (!(o instanceof Example)) { 
            return false; 
        } 
        Example e = (Example) o; 
        return comparePixels(this.getImage(),e.getImage());

    }
 	
 	public String toString() {
 		String str = "";
 		for (int i=0;i<28;i++) {
 			for (int j=0;j<28;j++) {
 				str = str + this.getImage()[i][j] + " \t";
 			}
 			str = str + "\n";
 		}
 		return str;
 	}
}
