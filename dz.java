import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.logging.*;
import java.util.Date;

public class JavaUnit04KalininPavel {

    static Logger logger = Logger.getLogger(JavaUnit04KalininPavel.class.getName());
    static final String LOG_FILE = "log.txt";
    static final String CHARSET_FILE = "UTF-8";
    static final String CHARSET_CONSOLE = "CP866";
    static final String INDENT = "\t\t";
    
    static BufferedReader reader = null;

    public static void main(String[] args) throws Exception {
        // Инициализация логера
        loggerInit(LOG_FILE);

        String taskText = "Задание №1 Реализовать алгоритм пирамидальной сортировки (HeapSort).\n";
        // Для одновременного вывода информации и в консоль и в лог придется использовать WARNING
        // т.к. все что ниже INFO не хочет работать.
        logger.warning(taskText); 

        int len = 10; // Кол-во элементов массива.
        //double[] A = {1,7,4,9,2,3,5,6,8,0};
 
        // Ввод кол-ва элементо массива из консоли.
        Integer number = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(System.in, CHARSET_CONSOLE);
            reader = new BufferedReader(inputStreamReader);
            while(true) {
                String s = getDataFromUser("Введите кол-во элементов массива (натуральное число): ");
                number = null;
                try { number = Integer.valueOf(s); } catch (Exception e) { logger.info(e.getMessage()); }
                if (number == null || number <= 0) 
                    logger.warning(s+" - не подходит.");
                else break;
            }    
        } catch (IOException e) { logger.severe(e.getMessage());
        } finally {
            try { inputStreamReader.close(); 
            } catch (IOException e) { logger.severe(e.getMessage()); }
        }    

        len = number;
        double[] A = getArray(len);

        logger.warning("Числовой массив перед сортировкой:");
        logger.warning( arrayToString(A, ", ") );
        double[] P = new double[len];
        pyramidSort(A, P, true);
        logger.warning("Числовой массив после сортировки:");
        logger.warning( arrayToString(A, ", ") );
        logger.info("Проверка результата.");
        for(int i=0; i<A.length-1; i++)
            if (A[i]>A[i+1]) 
            logger.warning("Ошибка "+i);
        logger.info("Программа закончена.");
    }

    public static void pyramidSort(double[] A, double[] P, boolean aIsLog) {
        int len = A.length;
        if(len == 0) return;
        // Создать Пирамиду.
        if(aIsLog) logger.info("\nСоздать Пирамиду");
        P[0] = A[0];
        for(int a=1, p=0; a<len; p=(++a-1)/2) { // p - parent
            P[a]=A[a];            
            if(P[p]>P[a]) {
                int c = a; // child
                int f = (c-1)/2; // father
                while(c>0 && P[f]>P[c]) { 
                    if(aIsLog) {
                        String s = String.format(" child=%d %f, father=%d %f",c,P[c],f,P[f]);
                        logger.info("восстановить "+s);
                    }     
                    double c_temp = P[c]; P[c] = P[f]; P[f]=c_temp; 
                    c = f;
                    f = (c-1)/2;
                }
            }    
        }

        if(aIsLog) logger.warning("\nПирамида:");
        if(aIsLog) logger.warning( arrayToString(P, ", ") );

        // Заполнить отсортированный массив. 
        if(aIsLog) logger.info("\nЗаполнить отсортированный массив");
        for(int a=0; a<len; a++) { // p - parent
            A[a] = P[0]; // верхушка Пирамиды - это минимальный элемент
            int last = len-1-a;
            if(last>0) { // если остались элементы в Пирамиде кроме верхушки
                P[0] = P[last]; // последний элемент поместить в вехушку Пирамиды
                // Восстановить порядок в Пирамиде.
                int f = 0; // father
                int l = 1; // сын слева
                int r = 2; // сын справа
                boolean isChange = true;
                while(isChange) {
                    isChange = false;
                    if(r <= last && P[r]<P[l] && P[r]<P[f]) { 
                        if(aIsLog) {
                            String s = String.format(" father=%d %f, left=%d %f, right=%d %f ",f,P[f],l,P[l],r,P[r]);
                            logger.info("обмен "+s);
                        }     
                        double r_temp = P[r]; P[r] = P[f]; P[f]=r_temp;
                        f = r;
                        isChange = true;
                    } else if(l <= last && P[l]<P[f]) { 
                        if(aIsLog) {
                            String s = String.format(" father=%d %f, left=%d %f ",f,P[f],l,P[l]);
                            logger.info("обмен "+s);
                        }     
                        double l_temp = P[l]; P[l] = P[f]; P[f]=l_temp;
                        f = l;
                        isChange = true;
                    }  
                    l = 2*f+1; // сын слева
                    r = 2*f+2; // сын справа
                }    
            }
        }
    }

    // создать и заполнить радндомно числовой массив
    public static double[] getArray(int aLen) {
        double[] ar = new double[aLen]; 
        Random random = new Random();
        for(int i=0; i<aLen; i++)
            ar[i] = ((int)( 10000 * random.nextDouble() ))/100.0 
                    * ((random.nextDouble()>0.5)?1:-1);
        return ar;                    
    }

    // массив в строку
    public static String arrayToString(double[] aAr, String aSeparator) {
        int len = aAr.length;
        if (len == 0) return "";
        // использование StringBuilder для формирования итоговой строки
        StringBuilder sb = new StringBuilder(len * 10);
        for(int i=0; i<len; i++)
            sb.append(aAr[i]).append(aSeparator);
        sb.delete(sb.length()-aSeparator.length(), sb.length()); // убрать последний разделитель
        return sb.toString();
    }

    public static String getDataFromUser(String aText) throws IOException {
        logger.warning(aText);
        String s = null;
        if (reader != null) 
            s = reader.readLine();
        else logger.info("Отстуствует инструмент приема данных от пользователя.");
        if (s == null) s = "";
        logger.info("Пользователь ввел : " + s);
        return s;
    }    

    // Инициализация логера
    public static void loggerInit(String aFileName) {
        FileHandler fh = null;
        try {
             fh = new FileHandler(aFileName, true);
        } catch (Exception e) {
            System.out.println("Проблемы с файлом "+aFileName+" "+e.getMessage());
        } 
        if(fh == null) System.exit(0);     
        try {
            fh.setEncoding(CHARSET_FILE);
        } catch (Exception e) {
           System.out.println("Проблемы с кодировкой FileHandler "+e.getMessage());
        } 
        fh.setLevel(Level.INFO); // все что ниже INFO не работает, зараза.
        //fh.setLevel(Level.FINE);
        logger.addHandler(fh);
      
//        SimpleFormatter sFormat = new SimpleFormatter();
//        fh.setFormatter(sFormat);
        fh.setFormatter(withoutRipplesInTheEyesFormatter);

        // Изменение консольного логера, которые уже создан по умолчанию
        for (Handler h : LogManager.getLogManager().getLogger("").getHandlers()) {
            if (h instanceof ConsoleHandler) {
                if (h.getFormatter() == null || !(h.getFormatter() instanceof EmptyFormatter)) {
                        h.setFormatter(emptyFormatter);
                    try {
                        h.setEncoding(CHARSET_CONSOLE);
                        h.setLevel(Level.WARNING); // все что ниже INFO не работает, зараза.
                        //h.setLevel(Level.INFO);
                    } catch (Exception e) {
                       System.out.println("Проблемы с кодировкой ConsoleHandler "+e.getMessage());
                    } 
                    //break; 
                }
            }
        }         
/*      // он там по умолчанию создан   
        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(sFormat);
        try {
            ch.setEncoding(CHARSET_CONSOLE);
        } catch (Exception e) {
           System.out.println("Проблемы с кодировкой ConsoleHandler "+e.getMessage());
        } 
        logger.addHandler(ch);
*/
        logger.info(INDENT+"\n\n------------------------------------------------------------\n");
        logger.info(INDENT+"Логирование инициализировано");
    }

    // Создание пустого формата для консоли
    static class EmptyFormatter extends Formatter {
        String lineSeparator = System.getProperty("line.separator");
        @Override public synchronized String format(LogRecord record) {
            return formatMessage(record)+lineSeparator;
        }
    }
    static EmptyFormatter emptyFormatter = new EmptyFormatter();

    static final Date dat = new Date();

    // Создание формата для файла лога, чтобы не рябило в глазах.
    static class WithoutRipplesInTheEyesFormatter extends Formatter {
        String lineSeparator = System.getProperty("line.separator");
        String yyyy_MM_dd_HH_mm_ss = "yyyy.MM.dd HH:mm:ss";
        @Override public synchronized String format(LogRecord record) {
            dat.setTime(record.getMillis());
            String dateStr = (new SimpleDateFormat( yyyy_MM_dd_HH_mm_ss )).format(dat);
            String message = formatMessage(record);
            String throwable = "";
            if (record.getThrown() != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.print("");
                record.getThrown().printStackTrace(pw);
                pw.close();
                throwable = sw.toString();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(dateStr)
                .append(" ")
                .append(((record.getSourceClassName() != null) ? record.getSourceClassName() : record.getLoggerName()))
                .append(" ")
                .append(((record.getSourceMethodName() != null) ? record.getSourceMethodName() : ""))
                .append("\t")
                .append(record.getLevel().getName())
                .append("  ")
                .append(message)
                .append(throwable)
                .append(lineSeparator);
            return sb.toString();
        }
    }
    static WithoutRipplesInTheEyesFormatter withoutRipplesInTheEyesFormatter 
        = new WithoutRipplesInTheEyesFormatter();

}