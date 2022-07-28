import java.util.ArrayList;
import java.util.Random;

// Синхронизированный блок кода

public class Worker {
	
	private ArrayList<Integer> list1 = new ArrayList<Integer>();
	private ArrayList<Integer> list2 = new ArrayList<Integer>();
	private Random random = new Random(); // генератор рандом цисел
	private Object lock1 = new Object();
	private Object lock2 = new Object();
	
	private void partOne() { // будет симуляция отправки запросов (например пинг машины)
		synchronized (lock1) { // сделали отдельные объекты для синхронизации и теперь  t2 поток ждет, пока t1 выполнит partOne и когда t1 начинает выполнять partTwo,
			// он освобождает lock1 и занимает lock2. И тогда на совобожденный lock1 прыгает поток t2.
			// таким образом мы сделали, что методы partOne и partTwo немогут выполняться двумя потоками одновременно.
			//Однако,например, елси partOne выполняется t1, а partTwo выполняется t2 одновременно - это возможно.
			try {
				Thread.sleep(1);  // съэмитируем процессорное время на выполнениен команды пинг , ставим слип на 1 милисекунду
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			list1.add(random.nextInt(100));  // Добавляем ип, но у нас их нет, поэтому сэмулируем и сгенерим число и добавим его
		}
		
	}
	
	private void partTwo() {  //  будет симуляция получения ответов  (например пинг машины)
		
		synchronized (lock2) {
			try {
				Thread.sleep(1);  
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			list2.add(random.nextInt(100));  // Добавляем ип, но у нас их нет, поэтому сэмулируем и сгенерим число и добавим его
		}
	
		
	}
	
	private  void proceed() {  // Выполнить процедуру
		for(int i = 0;i < 1000; i++) {  // якобы у нас 2000 пк. Сделаем ниже по 1000 в два потока
			partOne();
			partTwo();
		}
		
	}
	
	public void start() {  // Для старта весь этот процесс.        public - чтобы можно было вызвать из класса Main
		System.out.println("Начинаем...");
		//Дальше выводим время, которое заняла вся наша процедура и количество элементов в этих листах (list1, list2)
		// нужно высчитать потраченное время - от времени конечного вычитаем время начальное
		// Сложим текущее время в милисек в переменную long
		long startTime = System.currentTimeMillis(); 
		
		Thread t1 = new Thread(new Runnable() { //поток t1 и создадим краткую версию имплументации системы Runnable
			
			@Override
			public void run() {
				proceed();
				
			}
		});
		
		
		Thread t2 = new Thread(new Runnable() { //поток t1 и создадим краткую версию имплументации системы Runnable
			
			@Override
			public void run() {
				proceed();
				
			}
		});
		t1.start();
		t2.start();
		
		try {
			t1.join(); // Говорит,что после первого потока, нужно присоединить следующий
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
		long endTime = System.currentTimeMillis();
		System.out.println("Потраченное время: " + (endTime - startTime) + "\n"
				+ "Элементов в List1: " + list1.size() + "\n"
				+ "Элементов в List2: " + list2.size());
	}
}
