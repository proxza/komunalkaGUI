import java.text.DecimalFormat;
import java.text.NumberFormat;

// Класс счетчика для газа
public class GasCounter {

    public static String gasCount(int oldGasCounter, int newGasCounter) {
        double result;
        int kybGas = 0; // Сколько потребило кубов газа
        double gasTariff = 7; // Тариф по умолчанию. Тариф с сайта 104.ua - 6.9579грн за куб. Округлил до 7грн
        NumberFormat newDouble = new DecimalFormat("#.##");

        // FIX если новый счетчик равен 0 (показания не изменились) то в минус не считаем!
        if (newGasCounter != 0) {
            kybGas = newGasCounter - oldGasCounter;
        }

        result = kybGas * gasTariff;

        KomunalkaGUI.gasKybs = kybGas;
        KomunalkaGUI.counter += result; // Прибавляем результат к общей сумме "всего"

        return "Оплата за газ: " + newDouble.format(result) + "грн (" + kybGas + " куб.)";
    }

}
