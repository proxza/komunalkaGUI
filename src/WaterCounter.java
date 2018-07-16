import java.text.DecimalFormat;
import java.text.NumberFormat;

// Класс счетчика для воды
public class WaterCounter {

    public static String waterCount(int oldWaterCounter, int newWaterCounter) {
        double result;
        int kybWaters = 0; // Сколько всего потребило кубов воды
        double waterTariff = 13; // Тариф по умолчанию. Тариф горводоканала 12,936грн. (с НДС). Округляем до 13 за куб.
        NumberFormat newDouble = new DecimalFormat("#.##");

        // FIX если новый счетчик равен 0 (показания не изменились) то в минус не считаем!
        if (newWaterCounter != 0) {
            kybWaters = newWaterCounter - oldWaterCounter;
        }

        result = kybWaters * waterTariff;

        KomunalkaGUI.waterKybs = kybWaters;
        KomunalkaGUI.counter += result; // Прибавляем результат к общей сумме "всего"

        return "Оплата за воду: " + newDouble.format(result) + "грн (" + kybWaters + " куб.)";
    }

}
