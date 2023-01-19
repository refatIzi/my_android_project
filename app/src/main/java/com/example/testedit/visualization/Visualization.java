package com.example.testedit.visualization;

import android.graphics.Color;

import java.util.regex.Pattern;

public class Visualization {

    final TextColor KEYWORDS_FIRST = new TextColor(
            Pattern.compile(
                    "\\b(self|def|as|assert|break|continue|del|elif|else|except|finally|for|from|global|if|import|in|pass|raise|return|try|while|with|yield)\\b"),
            Color.parseColor("#c56a77")


    );
    final TextColor KEYWORDS_SECOND = new TextColor(
            Pattern.compile(
                    "\\b(False|None|True|and|nonlocal|not|or|class|def|is|lambda)\\b"),
            Color.parseColor("#3e9cca")


    );

    final TextColor NUMBERS = new TextColor(
            Pattern.compile("(\\b(\\d*[.]?\\d+)\\b)"),
            Color.parseColor("#2f5f93")
    );
    //Built-in functions1 Встроенные функции 1
    final TextColor BUILT_IN_FUNCTIONS_FIRST = new TextColor(
            Pattern.compile("(\\b(passive|Options|dict()|slice()|object()|staticmethod()|str()|int()|bool()|super()|tuple()|bytearray()|float()|bytes()|type()|property()|list()|frozenset()|classmethod()|complex()|set())\\b)"),
            Color.parseColor("#2aa9b0")
    );
    //Built-in functions2 Встроенные функции 2
    final TextColor BUILT_IN_FUNCTIONS_SECOND = new TextColor(
            Pattern.compile("(\\b(min()|setattr()|abs()|all()|dir()|hex()|next()|any()|divmod()|id()|sorted()|ascii()|enumerate()|input()|oct()|max()|round()|\n" +
                    "bin()|eval()|exec()|isinstance()|ord()|sum()|filter()|issubclass()|pow()|iter()|print()|callable()|format()|delattr()|\n" +
                    "len()|chr()|range()|vars()|getattr()|locals()|repr()|zip()compile()|globals()|map()|reversed()|__import__()|hasattr()|hash()|memoryview())\\b)"),
            Color.parseColor("#cc7832")
    );

    final TextColor STRING_METHODS = new TextColor(
            Pattern.compile("(\\b(capitalize()|casefold()|center()|count()|encode()|endswith()|expandtabs()|find()|index()|isalnum()\n" +
                    "isalpha()|isascii()|isdigit()|isidentifier()|islower()|isnumeric()|isprintable()|isspace()\n" +
                    "istitle()|isupper()|join()|ljust()|lower()|lstrip()|rstrip()|maketrans()|partition()|replace()\n" +
                    "rfind()|rindex()|rjust()|rpartition()|rsplit()|split()|splitlines()|startswith()|strip()\n" +
                    "swapcase()|title()|translate()|upper()|zfill())\\b)"),
            Color.parseColor("#b3b102")
    );

    final TextColor LIST_METHODS = new TextColor(
            Pattern.compile("(\\b(append()|extend()|insert()|remove()|pop()|clear()|sort()|reverse()|copy())\\b)"),
            Color.parseColor("#b3b102")
    );

    final TextColor DICTIONARY_METHODS = new TextColor(
            Pattern.compile("(\\b(fromkeys()|get()|items()|keys()|popitem()|setdefault()|update()|values())\\b)"),
            Color.parseColor("#b3b102")

    );

    final TextColor WORKING_METHODS_FIRST = new TextColor(
            Pattern.compile("(\\b(read()|write()|tell()|seek()|close()|open()|closed|mode|name|softspace)\\b)"),
            Color.parseColor("#b3b102")
    );

    final TextColor ARGUMENT = new TextColor(
            Pattern.compile("(\\b(file_name|access_mode|Buffering)\\b)"),
            Color.parseColor("#784fae")
    );

    final TextColor SIGN = new TextColor(
            Pattern.compile("\\#(.*[\\n]+|$)"),
            Color.parseColor("#627b57")
    );
    /**
     * Регулятор с 2 # для выделения света текс и вывода инфомрауии о коде в проводнике
     */
    final TextColor INFORMATION = new TextColor(
            Pattern.compile("\\##(.*[\\n]+|$)"),
            Color.parseColor("#ab7e00")
    );
    final TextColor BRACKETS = new TextColor(
            Pattern.compile("[\\(\\)]"),
            Color.parseColor("#3e9cca")
    );
    final TextColor SQUARE_BRACKETS = new TextColor(
            Pattern.compile("[\\[\\]]"),
            Color.parseColor("#3e9cca")
    );
    final TextColor BRACES = new TextColor(
            Pattern.compile("[\\{\\}]"),
            Color.parseColor("#3e9cca")
    );
    /**
     * Регулятор для трех и менее ковычек
     */
    final TextColor INVERTED_COMMAS = new TextColor(
            Pattern.compile("\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\""),
            Color.parseColor("#b4794c")
    );
    /**
     * регулятор для подсвтеки одиночных букв
     */
    TextColor LETTERS = new TextColor(
            Pattern.compile("(\\b(q|w|e|r|t|y|u|i|o|p|a|s|d|f|g|h|j|k|l|z|x|c|v|b|n|m|Q|W|E|R|T|Y|U|I|O|P|A|S|D|F|G|H|J|K|L|Z|X|C|V|B|N|M)\\b)"),
            Color.parseColor("#648cb8")
    );
    static Visualization visualization = new Visualization();
    static final TextColor[] colors = {
            visualization.KEYWORDS_FIRST,
            visualization.KEYWORDS_SECOND,
            visualization.NUMBERS,
            visualization.BUILT_IN_FUNCTIONS_FIRST,
            visualization.BUILT_IN_FUNCTIONS_SECOND,
            visualization.STRING_METHODS,
            visualization.LIST_METHODS,
            visualization.DICTIONARY_METHODS,
            visualization.LETTERS,
            visualization.WORKING_METHODS_FIRST,
            visualization.ARGUMENT,
            visualization.SIGN,
            visualization.INVERTED_COMMAS,
            visualization.INFORMATION,
            visualization.BRACKETS,
            visualization.SQUARE_BRACKETS,
            visualization.BRACES
    };

    public static TextColor[] getColors() {
        return colors;
    }

    public class TextColor {
        public final Pattern pattern;
        public final int color;

        TextColor(Pattern pattern, int color) {
            this.pattern = pattern;
            this.color = color;
        }
    }
}
