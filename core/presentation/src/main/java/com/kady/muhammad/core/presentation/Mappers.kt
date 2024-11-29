package com.kady.muhammad.core.presentation

import android.content.Context
import com.kady.muhammad.core.domain.error.DataError

fun DataError.Network.toString(context: Context): String {
    val stringRes = when (this) {
        DataError.Network.REQUEST_TIMEOUT   -> R.string.data_error_network_request_timeout
        DataError.Network.TOO_MANY_REQUESTS -> R.string.data_error_network_too_many_requests
        DataError.Network.NO_INTERNET       -> R.string.data_error_network_no_internet
        DataError.Network.SERVER_ERROR      -> R.string.data_error_network_server_error
        DataError.Network.SERIALIZATION     -> R.string.data_error_network_serialization
        DataError.Network.UNKNOWN           -> R.string.data_error_network_unknown
    }
    return context.getString(stringRes)
}

fun getFlagEmojiForCurrency(currencyCode: String): String {
    val currencyToFlag = mapOf(
        "AED" to "🇦🇪", // United Arab Emirates
        "AFN" to "🇦🇫", // Afghanistan
        "ALL" to "🇦🇱", // Albania
        "AMD" to "🇦🇲", // Armenia
        "ANG" to "🇳🇱", // Netherlands Antilles
        "AOA" to "🇦🇴", // Angola
        "ARS" to "🇦🇷", // Argentina
        "AUD" to "🇦🇺", // Australia
        "AWG" to "🇦🇼", // Aruba
        "AZN" to "🇦🇿", // Azerbaijan
        "BAM" to "🇧🇦", // Bosnia and Herzegovina
        "BBD" to "🇧🇧", // Barbados
        "BDT" to "🇧🇩", // Bangladesh
        "BGN" to "🇧🇬", // Bulgaria
        "BHD" to "🇧🇭", // Bahrain
        "BIF" to "🇧🇮", // Burundi
        "BMD" to "🇧🇲", // Bermuda
        "BND" to "🇧🇳", // Brunei
        "BOB" to "🇧🇴", // Bolivia
        "BRL" to "🇧🇷", // Brazil
        "BSD" to "🇧🇸", // Bahamas
        "BTC" to "₿",    // Bitcoin (no flag, using symbol)
        "BTN" to "🇧🇹", // Bhutan
        "BWP" to "🇧🇼", // Botswana
        "BYN" to "🇧🇾", // Belarus
        "BYR" to "🇧🇾", // Belarus (old Ruble)
        "BZD" to "🇧🇿", // Belize
        "CAD" to "🇨🇦", // Canada
        "CDF" to "🇨🇩", // Democratic Republic of Congo
        "CHF" to "🇨🇭", // Switzerland
        "CLF" to "🇨🇱", // Chile
        "CLP" to "🇨🇱", // Chile
        "CNY" to "🇨🇳", // China
        "CNH" to "🇨🇳", // China Offshore
        "COP" to "🇨🇴", // Colombia
        "CRC" to "🇨🇷", // Costa Rica
        "CUC" to "🇨🇺", // Cuba
        "CUP" to "🇨🇺", // Cuba
        "CVE" to "🇨🇻", // Cape Verde
        "CZK" to "🇨🇿", // Czech Republic
        "DJF" to "🇩🇯", // Djibouti
        "DKK" to "🇩🇰", // Denmark
        "DOP" to "🇩🇴", // Dominican Republic
        "DZD" to "🇩🇿", // Algeria
        "EGP" to "🇪🇬", // Egypt
        "ERN" to "🇪🇷", // Eritrea
        "ETB" to "🇪🇹", // Ethiopia
        "EUR" to "🇪🇺", // European Union
        "FJD" to "🇫🇯", // Fiji
        "FKP" to "🇫🇰", // Falkland Islands
        "GBP" to "🇬🇧", // United Kingdom
        "GEL" to "🇬🇪", // Georgia
        "GGP" to "🇬🇬", // Guernsey
        "GHS" to "🇬🇭", // Ghana
        "GIP" to "🇬🇮", // Gibraltar
        "GMD" to "🇬🇲", // Gambia
        "GNF" to "🇬🇳", // Guinea
        "GTQ" to "🇬🇹", // Guatemala
        "GYD" to "🇬🇾", // Guyana
        "HKD" to "🇭🇰", // Hong Kong
        "HNL" to "🇭🇳", // Honduras
        "HRK" to "🇭🇷", // Croatia
        "HTG" to "🇭🇹", // Haiti
        "HUF" to "🇭🇺", // Hungary
        "IDR" to "🇮🇩", // Indonesia
        "ILS" to "🇮🇱", // Israel
        "IMP" to "🇮🇲", // Isle of Man
        "INR" to "🇮🇳", // India
        "IQD" to "🇮🇶", // Iraq
        "IRR" to "🇮🇷", // Iran
        "ISK" to "🇮🇸", // Iceland
        "JEP" to "🇯🇪", // Jersey
        "JMD" to "🇯🇲", // Jamaica
        "JOD" to "🇯🇴", // Jordan
        "JPY" to "🇯🇵", // Japan
        "KES" to "🇰🇪", // Kenya
        "KGS" to "🇰🇬", // Kyrgyzstan
        "KHR" to "🇰🇭", // Cambodia
        "KMF" to "🇰🇲", // Comoros
        "KPW" to "🇰🇵", // North Korea
        "KRW" to "🇰🇷", // South Korea
        "KWD" to "🇰🇼", // Kuwait
        "KYD" to "🇰🇾", // Cayman Islands
        "KZT" to "🇰🇿", // Kazakhstan
        "LAK" to "🇱🇦", // Laos
        "LBP" to "🇱🇧", // Lebanon
        "LKR" to "🇱🇰", // Sri Lanka
        "LRD" to "🇱🇷", // Liberia
        "LSL" to "🇱🇸", // Lesotho
        "LTL" to "🇱🇹", // Lithuania
        "LVL" to "🇱🇻", // Latvia
        "LYD" to "🇱🇾", // Libya
        "MAD" to "🇲🇦", // Morocco
        "MDL" to "🇲🇩", // Moldova
        "MGA" to "🇲🇬", // Madagascar
        "MKD" to "🇲🇰", // North Macedonia
        "MMK" to "🇲🇲", // Myanmar
        "MNT" to "🇲🇳", // Mongolia
        "MOP" to "🇲🇴", // Macau
        "MRU" to "🇲🇷", // Mauritania
        "MUR" to "🇲🇺", // Mauritius
        "MVR" to "🇲🇻", // Maldives
        "MWK" to "🇲🇼", // Malawi
        "MXN" to "🇲🇽", // Mexico
        "MYR" to "🇲🇾", // Malaysia
        "MZN" to "🇲🇿", // Mozambique
        "NAD" to "🇳🇦", // Namibia
        "NGN" to "🇳🇬", // Nigeria
        "NIO" to "🇳🇮", // Nicaragua
        "NOK" to "🇳🇴", // Norway
        "NPR" to "🇳🇵", // Nepal
        "NZD" to "🇳🇿", // New Zealand
        "OMR" to "🇴🇲", // Oman
        "PAB" to "🇵🇦", // Panama
        "PEN" to "🇵🇪", // Peru
        "PGK" to "🇵🇬", // Papua New Guinea
        "PHP" to "🇵🇭", // Philippines
        "PKR" to "🇵🇰", // Pakistan
        "PLN" to "🇵🇱", // Poland
        "PYG" to "🇵🇾", // Paraguay
        "QAR" to "🇶🇦", // Qatar
        "RON" to "🇷🇴", // Romania
        "RSD" to "🇷🇸", // Serbia
        "RUB" to "🇷🇺", // Russia
        "RWF" to "🇷🇼", // Rwanda
        "SAR" to "🇸🇦", // Saudi Arabia
        "SBD" to "🇸🇧", // Solomon Islands
        "SCR" to "🇸🇨", // Seychelles
        "SDG" to "🇸🇩", // Sudan
        "SEK" to "🇸🇪", // Sweden
        "SGD" to "🇸🇬", // Singapore
        "SHP" to "🇸🇭", // Saint Helena
        "SLL" to "🇸🇱", // Sierra Leone
        "SOS" to "🇸🇴", // Somalia
        "SRD" to "🇸🇷", // Suriname
        "SSP" to "🇸🇸", // South Sudan
        "STD" to "🇸🇹", // São Tomé and Príncipe (old)
        "STN" to "🇸🇹", // São Tomé and Príncipe (new)
        "SYP" to "🇸🇾", // Syria
        "SZL" to "🇸🇿", // Eswatini (Swaziland)
        "THB" to "🇹🇭", // Thailand
        "TJS" to "🇹🇯", // Tajikistan
        "TMT" to "🇹🇲", // Turkmenistan
        "TND" to "🇹🇳", // Tunisia
        "TOP" to "🇹🇴", // Tonga
        "TRY" to "🇹🇷", // Turkey
        "TTD" to "🇹🇹", // Trinidad and Tobago
        "TWD" to "🇹🇼", // Taiwan
        "TZS" to "🇹🇿", // Tanzania
        "UAH" to "🇺🇦", // Ukraine
        "UGX" to "🇺🇬", // Uganda
        "USD" to "🇺🇸", // United States
        "UYU" to "🇺🇾", // Uruguay
        "UZS" to "🇺🇿", // Uzbekistan
        "VEF" to "🇻🇪", // Venezuela (old)
        "VES" to "🇻🇪", // Venezuela (new)
        "VND" to "🇻🇳", // Vietnam
        "VUV" to "🇻🇺", // Vanuatu
        "WST" to "🇼🇸", // Samoa
        "XAF" to "🇨🇲", // Central African CFA Franc (Cameroon as representative)
        "XCD" to "🇦🇬", // East Caribbean Dollar (Antigua and Barbuda as representative)
        "XOF" to "🇨🇮", // West African CFA Franc (Ivory Coast as representative)
        "XPF" to "🇵🇫", // CFP Franc (French Polynesia as representative)
        "YER" to "🇾🇪", // Yemen
        "ZAR" to "🇿🇦", // South Africa
        "ZMW" to "🇿🇲", // Zambia
        "ZWL" to "🇿🇼"  // Zimbabwe
    )
    return currencyToFlag[currencyCode.uppercase()] ?: "🏳️" // Default flag for unknown currency
}