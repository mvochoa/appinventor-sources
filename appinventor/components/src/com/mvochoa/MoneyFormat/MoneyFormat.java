package com.mvochoa.MoneyFormat;

import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.annotations.PropertyCategory;

/**
 * La extension MoneyFormat Cambia el formato de numérico a formato de moneda y viceversa:
 * - NumberToMoney: Convierte un numero en formato numérico "12345.34" a formato de moneda "$12,345.34".
 * - MoneyToNumber: Convierte un numero en formato de moneda "$12,345.34" a formato numérico "12345.34".
 *
 * @author mario@mvochoa.com (Mario Valentin Ochoa Mota)
 */

@DesignerComponent(version = 1,
description = "La extension MoneyFormat Cambia el formato de numérico a formato de moneda y viceversa:" +
"<ul>" +
"<li> <b>NumberToMoney:</b> Convierte un numero en formato numérico \"12345.34\" a formato de moneda \"$12,345.34\".</li>" +
"<li> <b>MoneyToNumber:</b> Convierte un numero en formato de moneda \"$12,345.34\" a formato numérico \"12345.34\".</li>" +
"</ul>",
category = ComponentCategory.EXTENSION, nonVisible = true,
iconName = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAArlJREFUOBFlU01IVFEU/s59bxTLRURpFEUrbRMVTU26CFdBSy1HwQgqjDILW1gLpWUlCRHFCEl/EJEaQlBUFi1cpGWmZElpBrkph2z6VZl5756+N4pOdOHyzj33fN/5zrnnCbg0XpOLxGQZzWAXQnQACh+QMO0RqNOJpTmdsvzqryA+c4mOVWyBh3bSrIXIE36bGJCEY0JQzYLFGX43QDAOmKgU3H6eSeDOg2e9Y8h2+5G0pfA1TLKNVLE+faVYA7F3aK/OJDCU6M07VPdhxovD6gH6J6SgvRhq9i/cy0LsnNOFhg4B3iVmW0dfK8S5S/BXOKEpHa0sgfpV7AeFyCiMYey/yyX4LGurprw8XpVB7WmSvYaXyp4jHSGwHBLqhk1eYMzTTArRj1VHkPKa2KQe7geUPESSOFzXsg8OftNO2no2chOBJMU7NjgmW9v6AiIDz9sNYw8zYIgdP0bJXcw8CD/VjelUEWb8N2ptHct4zzKaGbcHnu3RZ+X1AYHoh8pqNq2Z9nBagSPDVKBIOaP4lnrM8vPEmFq4Sy4jJyH4gVwmmCCZgXFKDKytITjI0MUdJftNjlAMidTONFgkiUhbDN73iwRPMWUv/a6qGvjeCUO2Fso6x+y7aN+DykGWwmZicyCRgVnordiL/EV1EBOl6E+BP71EwiQwNXCcBjpu8LKEkPMka+R5/s1V7XV8/nMfjnzBtvYdAgykCaAeCSQG355iKjYS/czSwNqucR5ezgYxQgJyzafkRhEJpmJ89k76+KtUDPJAAKfMailrLCTRCsKOY5LPJ7qMM1CEyK2hAKwvotv5vA+ZMMQkEf5MlWF42kHZOdyP2APOgcY5PJbvr/hpm9iHVQRzWuUty2mhPc0kR6W44wrLobj/fmespNtn0BgH6yQSXhHPEZ4XM/oVe9YqkbaRAPsXpdA/CzwEZE0AAAAASUVORK5CYII=")
@SimpleObject(external = true)
public class MoneyFormat extends AndroidNonvisibleComponent {

    private String coinSymbol = "$";

    public MoneyFormat(ComponentContainer container) {
        super(container.$form());
    }

    /**
     * NumberToMoney Convierte un numero en formato numérico "12345.34"
     * a formato de moneda "$12,345.34".
     *
     * @param number
     * @return El numero en formato de Moneda "$0,000.00"
     */
    @SimpleFunction(description = "Convierte un numero en formato numérico \"12345.34\"" +
     "a formato de moneda \"$12,345.34\".")
    public String NumberToMoney(String number) {
        try {
            String num = String.format("%.2f", Double.parseDouble(number)); // Se comprueba que sea un numero
            String[] numArray = num.split("\\.");
            for (int i = numArray[0].length() - 3; i > 0; i -= 3) {
                numArray[0] = String.format("%s,%s", numArray[0].substring(0, i),
                        numArray[0].substring(i, numArray[0].length()));
            }

            return String.format("%s%s.%s", coinSymbol, numArray[0], numArray[1]);
        } catch (Exception e) {
            return number;
        }
    }

    /**
     * MoneyToNumber Convierte un numero en formato de moneda "$12,345.34"
     * a formato numérico "12345.34".
     *
     * @param numberMoney
     * @return El numero en formato numérico "0000.00"
     */
    @SimpleFunction(description = "Convierte un numero en formato de moneda \"$12,345.34\"" +
    " a formato numérico \"12345.34\".")
    public String MoneyToNumber(String numberMoney) {
        return numberMoney.replaceAll("[" + coinSymbol + ",]", "");
    }

    /**
     * CoinSymbol Se especifica el símbolo de la moneda que se va usar como por $, €, ƒ, etc.
     *
     * @param coinSymbol
     */
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING, defaultValue = "$")
    @SimpleProperty(description = "Especifica el símbolo de la moneda.")
    public void CoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    /**
     * CoinSymbol Devuelve el símbolo de la moneda que se esta usando.
     *
     * @return Un cadena con el símbolo de la moneda.
     */
    @SimpleProperty(description = "Devuelve el símbolo de la moneda.", category = PropertyCategory.APPEARANCE)
    public String CoinSymbol() {
        return this.coinSymbol;
    }
}