
// This program extends Java thread class
public class MulthithreadingUsingThread extends Thread{
    @Override
    public void run() {
        try{
            System.out.println("Current thread's id is " + currentThread().getName());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[]args){
        long start = System.currentTimeMillis();
        for(int i = 0; i < 50; i++){
            MulthithreadingUsingThread obj = new MulthithreadingUsingThread();
            obj.start();
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
