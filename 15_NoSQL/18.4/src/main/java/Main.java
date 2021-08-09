import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        final MbLoader mongo = new MbLoader();

        System.out.println("Введите команду или \\INFO ");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine().trim();
            String[] cleanInput = input.split("\\s+");
            if(cleanInput[0].equals("\\INFO"))
            {
                System.out.println("Пример: ДОБАВИТЬ_МАГАЗИН Девяточка  \n" +
                        "Сначала укажите название команды, затем название магазина. В одно слово, без пробелов \n" +
                        "Пример: ДОБАВИТЬ_ТОВАР Вафли 54 \n" +
                        "Сначала укажите название команды, затем название товара, в одно слово, без пробелов. Затем укажите целое число — цену товара в рублях \n" +
                        "Пример: ВЫСТАВИТЬ_ТОВАР Вафли Девяточка \n" +
                        "Сначала укажите название команды, затем название товара и магазина \n" +
                        "ПРИМЕР: СТАТИСТИКА_ТОВАРОВ \n" +
                        "Выведет статистику  \n" +
                        "ПРИМЕР: КОНЕЦ_СМЕНЫ \n" +
                        "Закроет выполнение программы");
            }
            if(cleanInput[0].equals("ДОБАВИТЬ_МАГАЗИН"))
            {
                mongo.addShop(cleanInput[1].trim());
                System.out.println("Магазин добавлен");
            }
            if(cleanInput[0].equals("ДОБАВИТЬ_ТОВАР"))
            {
                mongo.addProduct(cleanInput[1].trim(),Integer.parseInt(cleanInput[2].trim()));
                System.out.println("Товар добавлен");
            }
            if(cleanInput[0].equals("ВЫСТАВИТЬ_ТОВАР"))
            {
                mongo.addInShop(cleanInput[1].trim(),cleanInput[2].trim());
            }
            if(cleanInput[0].equals("СТАТИСТИКА_ТОВАРОВ"))
            {
                System.out.println("СТАТИСТИКА");
                mongo.getStat();
            }
            if(cleanInput[0].equals("КОНЕЦ_СМЕНЫ"))
            {
                break;
            }
        }

    }
}
