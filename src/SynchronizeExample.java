
public class SynchronizeExample extends Thread{

    private static int x;

    @Override
    public void run() {

//            System.out.println("current thread is " + currentThread().getName());
//            System.out.println("And the message is " + this.msg);

        try {

            x = x + 1;
            this.sleep(1000);
            System.out.println(x);
            System.out.println(currentThread().getName());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[]args){
        SynchronizeExample o1 = new SynchronizeExample();
        SynchronizeExample o2 = new SynchronizeExample();

        o1.start();
        o2.start();
    }
}

