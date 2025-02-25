package lab1.argParse;

public class ParserArguments {
    private String typeFile;
    private String typeSort;
    private String nameFile;

    public ParserArguments(String[] args) {
        typeFile = "csv";
        typeSort = "more";
        nameFile = "in.txt";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (i == args.length - 1) {
                System.out.println("Unknown argument " + arg);
            }
            if (arg.startsWith("-")) {
                switch (arg) {
                    case "-s" -> typeSort = args[++i];
                    case "-f" -> typeFile = args[++i];
                    case "-i" -> nameFile = args[++i];
                    default -> System.out.println("Unknown argument " + arg);
                }
            }
        }
    }

    public String getTypeSort() {
        return typeSort;
    }

    public String getTypeFile() {
        return typeFile;
    }

    public String getNameFile() {
        return nameFile;
    }
}
