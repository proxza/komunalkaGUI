import java.text.DecimalFormat;
import java.text.NumberFormat;

// Класс счетчика для света
public class LightCounter {

    public static String lightCount(int oldLightCounter, int newLightCounter) {
        double result;
        int killowatts = 0; // Сколько всего потребило кВт
        double lightTariff = 0.9; // Тариф по умолчанию 0.90коп до 100кВт
        NumberFormat newDouble = new DecimalFormat("#.##");

        // FIX если новый счетчик равен 0 (показания не изменились) то в минус не считаем!
        if (newLightCounter != 0) {
            killowatts = newLightCounter - oldLightCounter;
        }

        // Если килловатт больше 100, тариф повышается до 1грн 68коп.
        if (killowatts >= 100) {
            lightTariff = 1.68;
        }

        result = killowatts * lightTariff;

        KomunalkaGUI.lightWatts = killowatts;
        KomunalkaGUI.counter += result; // Прибавляем результат к общей сумме "всего"

        return "Оплата за свет: " + newDouble.format(result) + "грн (" + killowatts + " кВт)";
    }
}
