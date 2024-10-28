package com.mohakchavan.java_kotlin_lib

import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.Collections


fun main() {

    // Change Table Name here.
    val tableName = "BranchMaster"

    // Replace JSON format here. (Remove "\n")
    val jsonText = "{" +
            "        \"ContactNo\": \"9874563215\"," +
            "        \"CustomerSupportNo\": \"\"," +
            "        \"BranchLogo\": \"\"," +
            "        \"BranchEmailID\": \"split@grubbrr.com\"," +
            "        \"OwnerID\": \"467\"," +
            "        \"CompanyID\": \"299\"," +
            "        \"Address1\": \"1081, Holland Drive\"," +
            "        \"Address2\": \"\"," +
            "        \"CountryCurrency\": \"USD\"," +
            "        \"CurrencySymbol\": \"\$\"," +
            "        \"CountryID\": \"231\"," +
            "        \"CountryName\": \"USA\"," +
            "        \"StateID\": \"3930\"," +
            "        \"StateName\": \"Florida\"," +
            "        \"CityID\": \"43606\"," +
            "        \"CityName\": \"Boca Raton\"," +
            "        \"ZipCode\": \"\"," +
            "        \"TimeZone\": \"87\"," +
            "        \"SquareFeet\": \"\"," +
            "        \"StartDate\": \"Sep  3 2024 12:00AM\"," +
            "        \"ExpireDate\": \"Aug 22 2021 12:00AM\"," +
            "        \"TagLine\": \"\"," +
            "        \"Footer\": \"\"," +
            "        \"DeliveryAreaRadius\": \"\"," +
            "        \"DeliveryAreaTitle\": \"\"," +
            "        \"DeliveryCharges\": \"\"," +
            "        \"FreeDeliveryUpto\": \"\"," +
            "        \"DeliveryTime\": \"\"," +
            "        \"PickupTime\": \"\"," +
            "        \"Latitude\": \"26.4074775\"," +
            "        \"Longtitude\": \"-80.1088858\"," +
            "        \"DistanceType\": \"\"," +
            "        \"WorkingDays\": \"1,2,3,4,5,6,7\"," +
            "        \"InvoicePrefix\": \"\"," +
            "        \"InvoiceRefNo\": \"20\"," +
            "        \"IsDeleted\": \"False\"," +
            "        \"IsPunchRequired\": \"False\"," +
            "        \"RefAccessToken\": \"\"," +
            "        \"NoOfVisits\": \"False\"," +
            "        \"AmountSpent\": \"False\"," +
            "        \"NoOfVisitOrAmount\": \"0\"," +
            "        \"NoOfPoints\": \"0\"," +
            "        \"RefPin\": \"\"," +
            "        \"HomeScreenLogo\": \"\"," +
            "        \"CustomerSupportEmailID\": \"reals@gmail.com\"," +
            "        \"KDSRefreshTime\": \"0\"," +
            "        \"LevelUpLocationId\": \"\"," +
            "        \"LanguageSelectionInWelcomeScreen\": \"False\"," +
            "        \"LanguageID\": \"1\"," +
            "        \"LanguageIDs\": \"\"," +
            "        \"TelePhoneNumberID\": \"1\"," +
            "        \"UnitID\": \"1\"," +
            "        \"ReciptFormatID\": \"1\"," +
            "        \"AlcoholLegalAge\": \"21\"," +
            "        \"IsUseCurrencyPrefix\": \"False\"," +
            "        \"CurrencyId\": \"1\"," +
            "        \"DateFormatID\": \"1\"," +
            "        \"IsDemoLocation\": \"True\"," +
            "        \"InheritMenuLevel\": \"2\"," +
            "        \"IsActive\": \"True\"," +
            "        \"CreatedBy\": \"590\"," +
            "        \"CreatedDate\": \"Sep  3 2024 10:03AM\"," +
            "        \"UpdatedBy\": \"590\"," +
            "        \"UpdatedDate\": \"Sep  4 2024  5:52AM\"," +
            "        \"IPAddress\": \"202.131.96.138:50768\"" +
            "    }"

    val json = JSONObject(jsonText)

    try {
        if (!json.isEmpty) {
            val fileDao = File("./DAO.txt")
            if (fileDao.exists()) {
                fileDao.delete()
            }
            val fileEnt = File("./Entity.txt")
            if (fileEnt.exists()) {
                fileEnt.delete()
            }

            val keys = json.keySet().toList()
            Collections.sort(keys, Comparator.comparing { it.lowercase() })

            var bufferedWriter = BufferedWriter(FileWriter(fileDao))
            bufferedWriter.run {
                write("@Dao\ninterface ${tableName}Dao {" +
                        "\ncompanion object {" +
                        "\nconst val TABLE_${tableName.uppercase()}=\"$tableName\"")

                for (key in keys) {
                    write("\n\nconst val ${tableName.uppercase()}_${key.uppercase()}=\"${key}\"")
                }
                write("\n}\n}")
            }
            bufferedWriter.flush()
            bufferedWriter.close()

            bufferedWriter = BufferedWriter(FileWriter(fileEnt))
            bufferedWriter.run {
                write("@Entity(tableName=${tableName}Dao.TABLE_${tableName.uppercase()})" +
                        "\ndata class ${tableName}(")
                for (key in keys) {
                    write("\n\n@SerializedName(\"$key\")" +
                            "\n@ColumnInfo(name=${tableName}Dao.${tableName.uppercase()}_${key.uppercase()})" +
                            "\nvar ${getVariableName(key)}:String?=null, // \"${json[key]}\"")
                }
                write("\n)")
            }
            bufferedWriter.flush()
            bufferedWriter.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun getVariableName(key: String?): String {
    return key.takeIf { !it.isNullOrEmpty() }?.let { k ->
        k[0].lowercase() + k.subSequence(1, k.length)
    } ?: ""
}
