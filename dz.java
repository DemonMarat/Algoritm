//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.text.SimpleDateFormat;
//import java.util.Random;
//import java.util.logging.*;
//import java.util.Date;
//
//public class JavaUnit04KalininPavel {
//
//    static Logger logger = Logger.getLogger(JavaUnit04KalininPavel.class.getName());
//    static final String LOG_FILE = "log.txt";
//    static final String CHARSET_FILE = "UTF-8";
//    static final String CHARSET_CONSOLE = "CP866";
//    static final String INDENT = "\t\t";
//
//    static BufferedReader reader = null;
//
//    public static void main(String[] args) throws Exception {
//        // Инициализация логера
//        loggerInit(LOG_FILE);
//
//        String taskText = "Задание №1 Реализовать алгоритм пирамидальной сортировки (HeapSort).\n";
//        // Для одновременного вывода информации и в консоль и в лог придется использовать WARNING
//        // т.к. все что ниже INFO не хочет работать.
//        logger.warning(taskText);
//
//        int len = 10; // Кол-во элементов массива.
//        //double[] A = {1,7,4,9,2,3,5,6,8,0};
//
//        // Ввод кол-ва элементо массива из консоли.
//        Integer number = null;
//        InputStreamReader inputStreamReader = null;
//        try {
//            inputStreamReader = new InputStreamReader(System.in, CHARSET_CONSOLE);
//            reader = new BufferedReader(inputStreamReader);
//            while(true) {
//                String s = getDataFromUser("Введите кол-во элементов массива (натуральное число): ");
//                number = null;
//                try { number = Integer.valueOf(s); } catch (Exception e) { logger.info(e.getMessage()); }
//                if (number == null || number <= 0)
//                    logger.warning(s+" - не подходит.");
//                else break;
//            }
//        } catch (IOException e) { logger.severe(e.getMessage());
//        } finally {
//            try { inputStreamReader.close();
//            } catch (IOException e) { logger.severe(e.getMessage()); }
//        }
//
//        len = number;
//        double[] A = getArray(len);
//
//        logger.warning("Числовой массив перед сортировкой:");
//        logger.warning( arrayToString(A, ", ") );
//        double[] P = new double[len];
//        pyramidSort(A, P, true);
//        logger.warning("Числовой массив после сортировки:");
//        logger.warning( arrayToString(A, ", ") );
//        logger.info("Проверка результата.");
//        for(int i=0; i<A.length-1; i++)
//            if (A[i]>A[i+1])
//            logger.warning("Ошибка "+i);
//        logger.info("Программа закончена.");
//    }
//
//    public static void pyramidSort(double[] A, double[] P, boolean aIsLog) {
//        int len = A.length;
//        if(len == 0) return;
//        // Создать Пирамиду.
//        if(aIsLog) logger.info("\nСоздать Пирамиду");
//        P[0] = A[0];
//        for(int a=1, p=0; a<len; p=(++a-1)/2) { // p - parent
//            P[a]=A[a];
//            if(P[p]>P[a]) {
//                int c = a; // child
//                int f = (c-1)/2; // father
//                while(c>0 && P[f]>P[c]) {
//                    if(aIsLog) {
//                        String s = String.format(" child=%d %f, father=%d %f",c,P[c],f,P[f]);
//                        logger.info("восстановить "+s);
//                    }
//                    double c_temp = P[c]; P[c] = P[f]; P[f]=c_temp;
//                    c = f;
//                    f = (c-1)/2;
//                }
//            }
//        }
//
//        if(aIsLog) logger.warning("\nПирамида:");
//        if(aIsLog) logger.warning( arrayToString(P, ", ") );
//
//        // Заполнить отсортированный массив.
//        if(aIsLog) logger.info("\nЗаполнить отсортированный массив");
//        for(int a=0; a<len; a++) { // p - parent
//            A[a] = P[0]; // верхушка Пирамиды - это минимальный элемент
//            int last = len-1-a;
//            if(last>0) { // если остались элементы в Пирамиде кроме верхушки
//                P[0] = P[last]; // последний элемент поместить в вехушку Пирамиды
//                // Восстановить порядок в Пирамиде.
//                int f = 0; // father
//                int l = 1; // сын слева
//                int r = 2; // сын справа
//                boolean isChange = true;
//                while(isChange) {
//                    isChange = false;
//                    if(r <= last && P[r]<P[l] && P[r]<P[f]) {
//                        if(aIsLog) {
//                            String s = String.format(" father=%d %f, left=%d %f, right=%d %f ",f,P[f],l,P[l],r,P[r]);
//                            logger.info("обмен "+s);
//                        }
//                        double r_temp = P[r]; P[r] = P[f]; P[f]=r_temp;
//                        f = r;
//                        isChange = true;
//                    } else if(l <= last && P[l]<P[f]) {
//                        if(aIsLog) {
//                            String s = String.format(" father=%d %f, left=%d %f ",f,P[f],l,P[l]);
//                            logger.info("обмен "+s);
//                        }
//                        double l_temp = P[l]; P[l] = P[f]; P[f]=l_temp;
//                        f = l;
//                        isChange = true;
//                    }
//                    l = 2*f+1; // сын слева
//                    r = 2*f+2; // сын справа
//                }
//            }
//        }
//    }
//
//    // создать и заполнить радндомно числовой массив
//    public static double[] getArray(int aLen) {
//        double[] ar = new double[aLen];
//        Random random = new Random();
//        for(int i=0; i<aLen; i++)
//            ar[i] = ((int)( 10000 * random.nextDouble() ))/100.0
//                    * ((random.nextDouble()>0.5)?1:-1);
//        return ar;
//    }
//
//    // массив в строку
//    public static String arrayToString(double[] aAr, String aSeparator) {
//        int len = aAr.length;
//        if (len == 0) return "";
//        // использование StringBuilder для формирования итоговой строки
//        StringBuilder sb = new StringBuilder(len * 10);
//        for(int i=0; i<len; i++)
//            sb.append(aAr[i]).append(aSeparator);
//        sb.delete(sb.length()-aSeparator.length(), sb.length()); // убрать последний разделитель
//        return sb.toString();
//    }
//
//    public static String getDataFromUser(String aText) throws IOException {
//        logger.warning(aText);
//        String s = null;
//        if (reader != null)
//            s = reader.readLine();
//        else logger.info("Отстуствует инструмент приема данных от пользователя.");
//        if (s == null) s = "";
//        logger.info("Пользователь ввел : " + s);
//        return s;
//    }
//
//    // Инициализация логера
//    public static void loggerInit(String aFileName) {
//        FileHandler fh = null;
//        try {
//             fh = new FileHandler(aFileName, true);
//        } catch (Exception e) {
//            System.out.println("Проблемы с файлом "+aFileName+" "+e.getMessage());
//        }
//        if(fh == null) System.exit(0);
//        try {
//            fh.setEncoding(CHARSET_FILE);
//        } catch (Exception e) {
//           System.out.println("Проблемы с кодировкой FileHandler "+e.getMessage());
//        }
//        fh.setLevel(Level.INFO); // все что ниже INFO не работает, зараза.
//        //fh.setLevel(Level.FINE);
//        logger.addHandler(fh);
//
////        SimpleFormatter sFormat = new SimpleFormatter();
////        fh.setFormatter(sFormat);
//        fh.setFormatter(withoutRipplesInTheEyesFormatter);
//
//        // Изменение консольного логера, которые уже создан по умолчанию
//        for (Handler h : LogManager.getLogManager().getLogger("").getHandlers()) {
//            if (h instanceof ConsoleHandler) {
//                if (h.getFormatter() == null || !(h.getFormatter() instanceof EmptyFormatter)) {
//                        h.setFormatter(emptyFormatter);
//                    try {
//                        h.setEncoding(CHARSET_CONSOLE);
//                        h.setLevel(Level.WARNING); // все что ниже INFO не работает, зараза.
//                        //h.setLevel(Level.INFO);
//                    } catch (Exception e) {
//                       System.out.println("Проблемы с кодировкой ConsoleHandler "+e.getMessage());
//                    }
//                    //break;
//                }
//            }
//        }
///*      // он там по умолчанию создан
//        ConsoleHandler ch = new ConsoleHandler();
//        ch.setFormatter(sFormat);
//        try {
//            ch.setEncoding(CHARSET_CONSOLE);
//        } catch (Exception e) {
//           System.out.println("Проблемы с кодировкой ConsoleHandler "+e.getMessage());
//        }
//        logger.addHandler(ch);
//*/
//        logger.info(INDENT+"\n\n------------------------------------------------------------\n");
//        logger.info(INDENT+"Логирование инициализировано");
//    }
//
//    // Создание пустого формата для консоли
//    static class EmptyFormatter extends Formatter {
//        String lineSeparator = System.getProperty("line.separator");
//        @Override public synchronized String format(LogRecord record) {
//            return formatMessage(record)+lineSeparator;
//        }
//    }
//    static EmptyFormatter emptyFormatter = new EmptyFormatter();
//
//    static final Date dat = new Date();
//
//    // Создание формата для файла лога, чтобы не рябило в глазах.
//    static class WithoutRipplesInTheEyesFormatter extends Formatter {
//        String lineSeparator = System.getProperty("line.separator");
//        String yyyy_MM_dd_HH_mm_ss = "yyyy.MM.dd HH:mm:ss";
//        @Override public synchronized String format(LogRecord record) {
//            dat.setTime(record.getMillis());
//            String dateStr = (new SimpleDateFormat( yyyy_MM_dd_HH_mm_ss )).format(dat);
//            String message = formatMessage(record);
//            String throwable = "";
//            if (record.getThrown() != null) {
//                StringWriter sw = new StringWriter();
//                PrintWriter pw = new PrintWriter(sw);
//                pw.print("");
//                record.getThrown().printStackTrace(pw);
//                pw.close();
//                throwable = sw.toString();
//            }
//            StringBuilder sb = new StringBuilder();
//            sb.append(dateStr)
//                .append(" ")
//                .append(((record.getSourceClassName() != null) ? record.getSourceClassName() : record.getLoggerName()))
//                .append(" ")
//                .append(((record.getSourceMethodName() != null) ? record.getSourceMethodName() : ""))
//                .append("\t")
//                .append(record.getLevel().getName())
//                .append("  ")
//                .append(message)
//                .append(throwable)
//                .append(lineSeparator);
//            return sb.toString();
//        }
//    }
//    static WithoutRipplesInTheEyesFormatter withoutRipplesInTheEyesFormatter
//        = new WithoutRipplesInTheEyesFormatter();
//
//}
//
////DZ 2
////Nodes {
////    doubleNode head; // Первичный элемен
////    public class doubleт Node
////    doubleNode tail; // Конечный элемент Node
////
////    private static class doubleNode {
////        doubleNode next; // ссылка на следующий элемент Node
////        doubleNode previous; // ссылка на предыдущий элемент Node
////        int value; // Значение элемента Node
////    }
////
////    // Добавляем новый элемент в начало. head смещается на 2-ую позицию.
////    public void addFirstElement(int value) {
////        doubleNode newNode = new doubleNode();
////        newNode.value = value;
////        if (head != null) {
////            newNode.next = head;
////            head.previous = newNode;
////        } else
////            tail = newNode;
////        head = newNode;
////    }
////
////    // Удаляем (обнуляем ссылку) на первый элемент. Второй смещается на первую.
////    public void removeFirstElement() {
////        if (head != null && head.next != null) {
////            head = head.next;
////            head.previous = null;
////        } else {
////            head = null;
////            tail = null;
////        }
////    }
////
////    // Ищем значение в списке, перебирая все элементы до первого нужного значения.
////    public boolean findElement(int value) {
////        doubleNode currentNode = head;
////        while (currentNode != null) {
////            if (currentNode.value == value) return true;
////            currentNode = currentNode.next;
////        }
////        return false;
////    }
////
////    // Добавление элемента в конец списка
////    public void addEndElement(int value) {
////        doubleNode newNode = new doubleNode();
////        newNode.value = value;
////        if (head == null) {
////            head = newNode;
////        } else {
////            newNode.previous = tail;
////            tail.next = newNode;
////        }
////        tail = newNode;
////    }
////
////    // Удалиение элемента с конца списка
////    public void removeEndElement() {
////        if (tail != null && tail.previous != null) {
////            tail = tail.previous;
////            tail.next = null;
////        } else {
////            head = null;
////            tail = null;
////        }
////    }
////
////    // Сортировка пузырьком от меньшего к большему
////    public void bubbleSort() {
////        boolean flag = true;
////        while (flag) {
////            flag = false;
////            doubleNode item = head;
////            while (item != null && item.next != null) {
////                if (item.value > item.next.value) {
////                    int temp = item.value;
////                    item.value = item.next.value;
////                    item.next.value = temp;
////                    flag = true;
////                }
////                item = item.next;
////            }
////        }
////    }
////
////    // Разворот двусвязного списка
////    public void reverse() {
////        if (head == null)
////            return;
////
////        doubleNode currentNode = head;
////        doubleNode nextCurrentNode = null;
////        doubleNode previousCurrentNode = null;
////
////        while (currentNode != null) {
////            nextCurrentNode = currentNode.next;
////            currentNode.next = previousCurrentNode;
////            currentNode.previous = nextCurrentNode;
////            previousCurrentNode = currentNode;
////            currentNode = nextCurrentNode;
////        }
////
////        currentNode = head;
////        head = tail;
////        tail = currentNode;
////    }
////}
// DZ 3

//import java.util.ArrayList;
//import java.util.List;
//
//public class RedBlackTree {
//
//    private Node root;
//
//    public boolean contain(int value) {
//        Node node = findNode(value, root);
//        return node != null;
//    }
//
//    private Node findNode(int value, Node midNode) {
//        if (midNode.value == value) {
//            return midNode;
//        } else if (midNode.value > value) {
//            if (midNode.leftChild != null) {
//                return findNode(value, midNode.leftChild);
//            } else {
//                return null;
//            }
//        } else {
//            if (midNode.rightChild != null) {
//                return findNode(value, midNode.rightChild);
//            } else {
//                return null;
//            }
//        }
//    }
//
//    public void add(int value) {
//        if (root == null) {
//            root = new Node(value);
//            root.color = Color.BLACK;
//        } else {
//            addNode(value, root);
//        }
//    }
//
//    private Node addNode(int value, Node node) {
//        if (contain(value)) {
//            return null;
//        } else if (node.value > value) {
//            if (node.leftChild == null) {
//                node.leftChild = new Node(value);
//                return node.leftChild;
//            } else {
//                Node addedNote = addNode(value, node.leftChild);
//                node.leftChild = rebalance(node.leftChild);
//                return addedNote;
//            }
//        } else {
//            if (node.rightChild == null) {
//                node.rightChild = new Node(value);
//                return node.rightChild;
//            } else {
//                Node addedNote = addNode(value, node.rightChild);
//                node.rightChild = rebalance(node.rightChild);
//                return addedNote;
//            }
//        }
//    }
//
//    private Node rebalance(Node node) {
//        Node result = node;
//        boolean needRebalance;
//        do {
//            needRebalance = false;
//            if (result.rightChild != null
//                    && result.rightChild.color == Color.RED
//                    && (result.leftChild == null || result.leftChild.color == Color.BLACK)) {
//                needRebalance = true;
//                result = rightSwap(result);
//            }
//            if (result.leftChild != null
//                    && result.leftChild.color == Color.RED
//                    && result.leftChild.leftChild != null && result.leftChild.leftChild.color == Color.RED) {
//                needRebalance = true;
//                result = leftSwap(result);
//            }
//            if (result.leftChild != null
//                    && result.leftChild.color == Color.RED
//                    && result.rightChild != null && result.rightChild.color == Color.RED) {
//                needRebalance = true;
//                colorSwap(result);
//            }
//        } while (needRebalance);
//        return result;
//    }
//
//    private void colorSwap(Node node) {
//        node.rightChild.color = Color.BLACK;
//        node.leftChild.color = Color.BLACK;
//        node.color = Color.RED;
//    }
//
//    private Node leftSwap(Node node) {
//        Node left = node.leftChild;
//        Node between = left.rightChild;
//        left.rightChild = node;
//        node.leftChild = between;
//        left.color = node.color;
//        node.color = Color.RED;
//        return left;
//    }
//
//    private Node rightSwap(Node node) {
//        Node right = node.rightChild;
//        Node between = right.leftChild;
//        right.leftChild = node;
//        node.rightChild = between;
//        right.color = node.color;
//        node.color = Color.RED;
//        return right;
//    }
//
//    private class Node {
//        private int value;
//        private Node leftChild;
//        private Node rightChild;
//        private Color color;
//
//        public Node(int value) {
//            this.value = value;
//            leftChild = null;
//            rightChild = null;
//            color = Color.RED;
//        }
//
//        @Override
//        public String toString() {
//            return "[" + value + "-" + color + "]";
//        }
//    }
//
//
//    // вывод дерева с урока
//
//    private class PrintNode {
//        Node node;
//        String str;
//        int depth;
//
//        public PrintNode() {
//            node = null;
//            str = " ";
//            depth = 0;
//        }
//
//        public PrintNode(Node node) {
//            depth = 0;
//            this.node = node;
//            this.str = node.toString();
//        }
//    }
//
//    public void print() {
//        int maxDepth = maxDepth() + 3;
//        int nodeCount = nodeCount(root, 0);
//        int width = 50;//maxDepth * 4 + 2;
//        int height = nodeCount * 5;
//        List<List<PrintNode>> list = new ArrayList<List<PrintNode>>();
//        for (int i = 0; i < height; i++) /*Создание ячеек массива*/ {
//            ArrayList<PrintNode> row = new ArrayList<>();
//            for (int j = 0; j < width; j++) {
//                row.add(new PrintNode());
//            }
//            list.add(row);
//        }
//
//        list.get(height / 2).set(0, new PrintNode(root));
//        list.get(height / 2).get(0).depth = 0;
//
//        for (int j = 0; j < width; j++)  /*Принцип заполнения*/ {
//            for (int i = 0; i < height; i++) {
//                PrintNode currentNode = list.get(i).get(j);
//                if (currentNode.node != null) {
//                    currentNode.str = currentNode.node.toString();
//                    if (currentNode.node.leftChild != null) {
//                        int in = i + (maxDepth / (int) Math.pow(2, currentNode.depth));
//                        int jn = j + 3;
//                        printLines(list, i, j, in, jn);
//                        list.get(in).get(jn).node = currentNode.node.leftChild;
//                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;
//
//                    }
//                    if (currentNode.node.rightChild != null) {
//                        int in = i - (maxDepth / (int) Math.pow(2, currentNode.depth));
//                        int jn = j + 3;
//                        printLines(list, i, j, in, jn);
//                        list.get(in).get(jn).node = currentNode.node.rightChild;
//                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;
//                    }
//
//                }
//            }
//        }
//        for (int i = 0; i < height; i++) /*Чистка пустых строк*/ {
//            boolean flag = true;
//            for (int j = 0; j < width; j++) {
//                if (list.get(i).get(j).str != " ") {
//                    flag = false;
//                    break;
//                }
//            }
//            if (flag) {
//                list.remove(i);
//                i--;
//                height--;
//            }
//        }
//
//        for (var row : list) {
//            for (var item : row) {
//                System.out.print(item.str + " ");
//            }
//            System.out.println();
//        }
//    }
//
//    private void printLines(List<List<PrintNode>> list, int i, int j, int i2, int j2) {
//        if (i2 > i) // Идём вниз
//        {
//            while (i < i2) {
//                i++;
//                list.get(i).get(j).str = "|";
//            }
//            list.get(i).get(j).str = "\\";
//            while (j < j2) {
//                j++;
//                list.get(i).get(j).str = "-";
//            }
//        } else {
//            while (i > i2) {
//                i--;
//                list.get(i).get(j).str = "|";
//            }
//            list.get(i).get(j).str = "/";
//            while (j < j2) {
//                j++;
//                list.get(i).get(j).str = "-";
//            }
//        }
//    }
//
//    public int maxDepth() {
//        return maxDepth2(0, root);
//    }
//
//    private int maxDepth2(int depth, Node node) {
//        depth++;
//        int left = depth;
//        int right = depth;
//        if (node.leftChild != null)
//            left = maxDepth2(depth, node.leftChild);
//        if (node.rightChild != null)
//            right = maxDepth2(depth, node.rightChild);
//        return left > right ? left : right;
//    }
//
//    private int nodeCount(Node node, int count) {
//        if (node != null) {
//            count++;
//            return count + nodeCount(node.leftChild, 0) + nodeCount(node.rightChild, 0);
//        }
//        return count;
//    }
//
//}