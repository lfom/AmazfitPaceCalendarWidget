package com.dinodevs.pacecalendarwidget;
import java.util.Arrays;

/**
 * Created by GreatApo on 08/04/2018.
 */

public class APtranslations {
    public static class translations {

        public static String[] codes = {
                "en", "zh", "cz", "fr", "de", "gr", "he", "hu", "it", "ja", "pl", "pt", "ru", "sk", "es", "nl"//, "tr",
        };

        public static String[] languages = {
                "English", "中文", "Czech", "Français", "Deutsch", "Ελληνικά", "עברית", "Magyar", "Italiano", "日本語", "Polski", "Português", "Русский", "Slovenčina", "Español", "Nederlands"//, "Türkçe",
        };

        public static String[] languages_en = {
                "English", "Chinese", "Czech", "French", "German", "Greek", "Hebrew", "Hungarian", "Italian", "Japanese", "Polish", "Portuguese", "Russian", "Slovak", "Spanish", "Dutch"//, "Turkish",
        };

        public static String[][] days = {
                {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"},
                {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"},
                {"Po", "Út", "St", "Čt", "Pá", "So", "Ne"},
                {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"},
                {"Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"},
                {"Κυριακή", "Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", "Σάββατο"},
                {"ש'","ו'","ה'","ד'","ג'","ב'","א'"},
                {"Vasárnap", "Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombaton"},
                {"Domenica", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"},
                {"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"},
                {"Niedziela", "Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota"},
                {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"},
                {"воскресенье", "понедельник", "вторник", "среда", "четверг", "пятница", "суббота"},
                {"Nedeľa", "Pondelok", "Utorok", "Streda", "Štvrtok", "Piatok", "Sobota"},
                {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"},
                {"Zondag", "Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag"}//,
                //{"Pazar", "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi"},

        };

        public static String[][] months = {
                {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"},
                {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"},
                {"Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"},
                {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "octobre", "Novembre", "Décembre"},
                {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"},
                {"Ιανουάριος", "Φεβρουάριος", "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος", "Ιούλιος", "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος", "Δεκέμβριος"},
                {"ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני", "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"},
                {"Január", "Február", "Március", "Április", "Május", "Június", "Július", "Augusztus", "Szeptember", "Október", "November", "December"},
                {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"},
                {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"},
                {"Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"},
                {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"},
                {"январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"},
                {"Január", "Február", "Marec", "Apríl", "Máj", "Jún", "Júl", "August", "September", "Október", "November", "December"},
                {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"},
                {"Januari", "Februari", "Maart", "April", "Mei", "Juni", "Juli", "Augustus", "September", "Oktober", "November", "December"}//,
                //{"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"},
        };

        public static String[][] other = {
                {"Select color","Show year","Monday 1st"},                  //"English",
                {"选择颜色","显示年份","星期一放1"},                          //"Chinese",
                {"Vyberte barvu", "Zobrazit rok", "Od pondělka"},           //"Czech",
                {"Choisissez la couleur","Afficher l'année","Mettez 1er"},  //"French",
                {"Farbe auswählen","Jahr anzeigen","Montag erste"},         //"German",
                {"Επιλογή χρώματος","Εμφάνιση χρονιάς","Δευτέρα 1η"},       //"Greek",
                {"בחירת צבע","הצג שנה","החל מיום שני"},                     //"Hebrew"
                {"Válasszon színt","Az év megjelenítése","Hétfő 1"},        //"Hungarian",
                {"Seleziona colore","Mostra l'anno","Lunedì 1°"},           //"Italian",
                {"色を選択","年を表示","月曜日に入れて"},                     //"Japanese",
                {"Wybierz kolor","Pokaż rok","Poniedziałek 1."},            //"Polish",
                {"Selecione a cor","Mostrar ano","Segunda-feira"},          //"Portuguese",
                {"Выбор цвета","Показ года","Пон. 1-й"},                    //"Russian",
                {"Vyberte farbu","Zobraziť rok","Pondelok 1."},             //"Slovak",
                {"Seleccionar el color","Mostrar año","Lunes 1°"},          //"Spanish"
                {"Selecteer kleur","Toon jaar","Maandag 1e"}               //"Dutch"
                //{"Renk seç","Yılı göster","Pazartesi 1"},                 //"Turkish"
        };

        public static String[] getDays(int lang) {
            return translations.days[lang % translations.codes.length];
        }

        public static String[] getMonths(int lang) {
            return translations.months[lang % translations.codes.length];
        }
    }

    // Default language
    private int lang_number = 0;

    APtranslations () {

    }
    APtranslations (String code) {
        this.setLang(code);
    }

    public String getCode() {
        return translations.codes[this.lang_number];
    }

    public String getName() {
        return translations.languages[this.lang_number];
    }

    public String[] getOther() {return translations.other[this.lang_number];}

    public void nextLang() {
        this.lang_number = (this.lang_number + 1) % translations.codes.length;
    }
    public void setLang(String code) {
        // Find lang number by code
        int index = Arrays.asList(translations.codes).indexOf(code);
        if (index >= 0) {
            this.lang_number = index;
        }
    }

    public String[] getDays() {
        return translations.getDays(this.lang_number);
    }

    public String[] getMonths() {return translations.getMonths(this.lang_number);}

    public boolean isRtL() {
        return (translations.codes[this.lang_number]=="he");
    }
}
