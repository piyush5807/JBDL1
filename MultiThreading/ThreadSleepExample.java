public class ThreadSleepExample extends Thread{

    @Override
    public void run() {
        for(int i=0;i<5;i++){
            System.out.println(currentThread().getName() + " " + i);

            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[]args){
        ThreadSleepExample t1 = new ThreadSleepExample();
        t1.start();
        ThreadSleepExample t2 = new ThreadSleepExample();
        t2.start();
    }
}
