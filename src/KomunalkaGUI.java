import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class KomunalkaGUI extends JFrame {
    private JButton cancelBtn, countBtn, reportBtn;
    private JTextField lightTField1, lightTField2, waterTField1, waterTField2, gasTField1, gasTField2;
    private JLabel lastCountTariffs, lightLabel1, lightLabel2, waterLabel1, waterLabel2, gasLabel1, gasLabel2, tariffLable, swingLabel;
    private JTextArea resultsAll;
    private JFileChooser saveFileTo;
    public static double counter; // Счетчик "всего" грн.
    public static int lightWatts; // Переменная для вывода "разницы" в ваттах для света
    public static int waterKybs; // Переменная для вывода "разницы" в куб.м. для воды
    public static int gasKybs; // Переменная для вывода "разницы" в куб.м* для газа
    private final String tmpFolderName = System.getenv("APPDATA") + "\\Kommunalka\\"; // Путь в AppData\Roaming с названием папки для файла с счетчиками
    private final String tmpCounter = tmpFolderName + "tmpCounters.dat"; // Название файла для тмпсчетчиков
    NumberFormat newDouble = new DecimalFormat("#.##"); // фикс отображения дробной части у "всего"
    eHandler handler = new eHandler();


    public KomunalkaGUI(String title) {
        // Инициилизация
        super(title); // Передаем заголовок (название) программы
        setLayout(new FlowLayout());

        addComponentsOnPanel(); // Создаем и располагаем на форме все компоненты (кнопки-лэйблы-и т.п.)

        // Главные настройки
        pack(); // Компонуем всё на форме для корректного отображения (ОБЯЗАТЕЛЬНЫЙ ПАРАМЕТР)
        setSize(380, 630); // Устанавливаем размеры формы
        Image icon = createIcon("/images/hola.png"); // Устанавливаем иконку в заголовке
        setIconImage(icon);
        setResizable(false); // Запрещаем масштабирование (растягивание) формы
        setLocationRelativeTo(null); // Позиционируем форму при старте по центру экрана
        setVisible(true); // Делаем форму видимой (ОБЯЗАТЕЛЬНЫЙ ПАРАМЕТР)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Определяем ивент для закрытия программы по нажатию на крестик (ОБЯЗАТЕЛЬНЫЙ ПАРАМЕТР)

    }


    private void addComponentsOnPanel() {
        // Кнопки
        cancelBtn = new JButton("Очистить");
        countBtn = new JButton("Посчитать");
        reportBtn = new JButton("Отчет");
        reportBtn.setEnabled(false); // Вырубаем кнопку "отчет" пока не сформируют отчет по кнопке "Подсчитать"

        // Лэйблы
        lastCountTariffs = new JLabel("Вы ещё не проводили подсчеты");
        lastCountTariffs.setPreferredSize(new Dimension(362, 20));
        lastCountTariffs.setHorizontalAlignment(SwingConstants.CENTER);
        lastCountTariffs.setForeground(Color.gray);

        lightLabel1 = new JLabel("Введите старые показания за свет:");
        lightLabel1.setPreferredSize(new Dimension(220, 20)); // Выставляем размер поля
        lightLabel2 = new JLabel("Введите новые показания за свет:");
        lightLabel2.setPreferredSize(new Dimension(220, 20)); // Выставляем размер поля

        waterLabel1 = new JLabel("Введите старые показания за воду:");
        waterLabel1.setPreferredSize(new Dimension(220, 20)); // Выставляем размер поля
        waterLabel2 = new JLabel("Введите новые показания за воду:");
        waterLabel2.setPreferredSize(new Dimension(220, 20)); // Выставляем размер поля

        gasLabel1 = new JLabel("Введите старые показания за газ:");
        gasLabel1.setPreferredSize(new Dimension(220, 20)); // Выставляем размер поля
        gasLabel2 = new JLabel("Введите новые показания за газ:");
        gasLabel2.setPreferredSize(new Dimension(220, 20)); // Выставляем размер поля

        tariffLable = new JLabel("Тарифы актуальны на 14.03.2018");
        tariffLable.setPreferredSize(new Dimension(370, 20));
        tariffLable.setHorizontalAlignment(SwingConstants.CENTER);
        tariffLable.setForeground(Color.gray);

        swingLabel = new JLabel("powered on Swing JAVA");
        swingLabel.setPreferredSize(new Dimension(220, 20)); // Выставляем размер поля
        swingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        swingLabel.setForeground(Color.gray);

        // TextArea для вывода результатов
        resultsAll = new JTextArea(20, 31);
        Border border = BorderFactory.createLineBorder(Color.BLACK); // Создаем бордер
        resultsAll.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        resultsAll.setEditable(false); // Запрещаем редактирование (но выделять и копировать можно!)

        // Текстовые поля для ввода
        lightTField1 = new JTextField(12);
        lightTField1.setText("0");
        lightTField2 = new JTextField(12);
        lightTField2.setText("0");
        waterTField1 = new JTextField(12);
        waterTField1.setText("0");
        waterTField2 = new JTextField(12);
        waterTField2.setText("0");
        gasTField1 = new JTextField(12);
        gasTField1.setText("0");
        gasTField2 = new JTextField(12);
        gasTField2.setText("0");

        getOldCounterFromFile();


        // Добавление элементов на форму
        add(lastCountTariffs);
        add(lightLabel1);
        add(lightTField1);
        add(lightLabel2);
        add(lightTField2);
        add(waterLabel1);
        add(waterTField1);
        add(waterLabel2);
        add(waterTField2);
        add(gasLabel1);
        add(gasTField1);
        add(gasLabel2);
        add(gasTField2);
        add(resultsAll);
        add(tariffLable);
        add(cancelBtn);
        add(countBtn);
        add(reportBtn);
        add(swingLabel);
        countBtn.addActionListener(handler); // Добавляем "слушателя" событий для кнопки "Посчитать"
        cancelBtn.addActionListener(handler); // Добавляем слушателя для кнопки "Очистить"
        reportBtn.addActionListener(handler); // Добавляем слушателя для кнопки "Отчет"

    }


    // Класс "Слушателя" для обработки ивентов по нажатиям на кнопки
    public class eHandler implements ActionListener {
        int oldLight, newLight, oldWater, newWater, oldGas, newGas;

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Если нажата кнопка - выполняем определенное действие
                if (e.getSource() == countBtn) { // Нажатие кнопки "Посчитать"

                    resultsAll.setText(null); // Очищаем текстовое поле с выводом на случай повторного нажатия на "Посчитать"
                    counter = 0; // Обнуляем статический счетчик что бы не наматывало по кругу на случай если нажать 10 раз "посчитать" :)

                    // replaceFirst("^0+(?!$)", "") <- Регулярка удаляющая начальные нули у строки (превращая строку вида: 000ХХХ в ХХХ)
                    oldLight = fixNullPointer(lightTField1.getText().replaceFirst("^0+(?!$)", ""));
                    newLight = fixNullPointer(lightTField2.getText().replaceFirst("^0+(?!$)", ""));

                    oldWater = fixNullPointer(waterTField1.getText().replaceFirst("^0+(?!$)", ""));
                    newWater = fixNullPointer(waterTField2.getText().replaceFirst("^0+(?!$)", ""));

                    oldGas = fixNullPointer(gasTField1.getText().replaceFirst("^0+(?!$)", ""));
                    newGas = fixNullPointer(gasTField2.getText().replaceFirst("^0+(?!$)", ""));

                    if (oldLight == 0 && newLight == 0 && oldWater == 0 && newWater == 0 && oldGas == 0 && newGas == 0) {
                        JOptionPane.showMessageDialog(null, "Введите показания хотя бы одного счетчика!", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
                    } else {

                        // Объявление стрингов ниже делается для того что бы методы выполнились раньше "вывода" для того что бы стали доступны статические переменные для вывода разницы
                        String lightTmp = LightCounter.lightCount(oldLight, newLight);
                        String waterTmp = WaterCounter.waterCount(oldWater, newWater);
                        String gasTmp = GasCounter.gasCount(oldGas, newGas);

                        // Добавляем полученное в textarea
                        resultsAll.append("Свет: \r\n");
                        resultsAll.append("Старые показания: " + oldLight + "\r\n");
                        resultsAll.append("Новые показания: " + newLight + "\r\n");
                        resultsAll.append("Разница в " + lightWatts + " кВт. \r\n\r\n");


                        resultsAll.append("Вода: \r\n");
                        resultsAll.append("Старые показания: " + oldWater + "\r\n");
                        resultsAll.append("Новые показания: " + newWater + "\r\n");
                        resultsAll.append("Разница в " + waterKybs + " куб. \r\n\r\n");


                        resultsAll.append("Газ: \r\n");
                        resultsAll.append("Старые показания: " + oldGas + "\r\n");
                        resultsAll.append("Новые показания: " + newGas + "\r\n");
                        resultsAll.append("Разница в " + gasKybs + " куб. \r\n\r\n");


                        resultsAll.append("Итого за месяц: \r\n");
                        resultsAll.append(lightTmp + "\r\n");
                        resultsAll.append(waterTmp + "\r\n");
                        resultsAll.append(gasTmp + "\r\n");
                        resultsAll.append("Всего: " + newDouble.format(counter) + "грн");


                        // Проверка, если TextArea НЕ пустое - то делаем активной кнопку "Отчет"
                        if (!resultsAll.getText().isEmpty()) {
                            reportBtn.setEnabled(true);
                        }

                        // Вызов метода для сохранения новых показаний счетчиков в тмп-файл для дальнейшей работы
                        setOldCounterToFile(lightTField2.getText(), waterTField2.getText(), gasTField2.getText());
                    }

                } else if (e.getSource() == cancelBtn) { // Нажатие кнопки "Очистить"

                    reportBtn.setEnabled(false);
                    clearButtonFunction();

                } else if (e.getSource() == reportBtn) { // Нажатие кнопки "Отчет"

                    saveTheReport();

                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Введите число!", "ОШИБКА", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    // Метод для встави иконки в заголовке
    protected Image createIcon(String path) {
        URL imgUrl = KomunalkaGUI.class.getResource(path); // Ссылка на ресурс (иконку)

        // Проверка - если ссылка не пустая (файл не отсутствует)
        if (imgUrl != null) {
            return new ImageIcon(imgUrl).getImage();
        } else {
            System.err.println("Файл иконки не найден: " + path);
            return null;
        }
    }


    // Метод проверки на пустоту введенных строк
    protected int fixNullPointer(String line) {
        if (!line.isEmpty()) {
            return Integer.parseInt(line); // Если поле не пустое
        } else {
            return 0;
        }
    }


    // Метод срабатывания кнопки очистки (чистит все поля)
    protected void clearButtonFunction() {
        lightTField1.setText("0");
        lightTField2.setText("0");
        waterTField1.setText("0");
        waterTField2.setText("0");
        gasTField1.setText("0");
        gasTField2.setText("0");
        resultsAll.setText(null);
    }


    // Метод для сохранения отчетов
    protected void saveTheReport() {

        try {

            saveFileTo = new JFileChooser();
            saveFileTo.setDialogTitle("Куда сохранить отчет?"); // Именуем заголовок окна "сохранить как"

            String fileName = formatDateToReport() + ".txt"; // Ставим дату и расширение для названия нашего файла

            File writer = new File(fileName); // создаем объект файла
            saveFileTo.setSelectedFile(writer); // помещаем его в объект JFileChooser

            if (saveFileTo.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { // Если выведено окошко "Сохранить" после нажатия кнопки "Отчет"
                File theFile = saveFileTo.getSelectedFile(); // Получаем наш объект файла
                String nameOfFile = theFile.getPath(); // Получаем выбранный путь

                BufferedWriter out = new BufferedWriter(new FileWriter(nameOfFile, true)); // Создаем поток с файлом
                out.append(resultsAll.getText()); // Заносим в него результат поля TextArea
                out.close(); // Закрываем поток

                JOptionPane.showMessageDialog(null, "Отчет успешно сохранен!", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка!", JOptionPane.INFORMATION_MESSAGE);
        }

    }


    // Метод для формирования даты по сегодняшнему дню (для названия файла отчета)
    protected String formatDateToReport() {
        DateFormat dt = new SimpleDateFormat("MM-dd-yyyy");
        Date today = Calendar.getInstance().getTime();
        String dateForReport = dt.format(today);

        return dateForReport;
    }


    // Метод получения из файла старых показаний счетчиков
    protected void getOldCounterFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(tmpCounter));
            String tmpRead = "";
            String[] arr = new String[3];

            while ((tmpRead = reader.readLine()) != null) {
                arr = tmpRead.split(":", 4);
            }

            if (!arr[0].isEmpty()) {
                lightTField1.setText(arr[0]);
            }

            if (!arr[1].isEmpty()) {
                waterTField1.setText(arr[1]);
            }

            if (!arr[2].isEmpty()) {
                gasTField1.setText(arr[2]);
            }

            lastCountTariffs.setText("Последний раз вы считали тарифы: " + arr[3]);

        } catch (FileNotFoundException ex) {
            // Проверка, если файла ТМП со счетчиками нет - создаем новый
            setOldCounterToFile("0", "0", "0");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка!", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    // Метод записи в файл новых показаний счетчиков
    protected void setOldCounterToFile(String lightC, String waterC, String gasC){
        try {

            File createTmpFolder = new File(tmpFolderName);
            if (!createTmpFolder.exists()) {
                createTmpFolder.mkdirs();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(tmpCounter));

            // Выставляем дату
            DateFormat dt = new SimpleDateFormat("dd.MM.yyyy");
            Date toDay = Calendar.getInstance().getTime();
            String date = dt.format(toDay);

            String tmp = lightC + ":" + waterC + ":" + gasC + ":" + date;
            writer.append(tmp);
            writer.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
