package esun.gordon;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        Main main = new Main();
        main.go();

    }

    private void go() throws Exception {

        List<String> rowList = Files.readAllLines(Paths.get("resources/hello.txt"));
        List<String> finalList = new ArrayList<>();

        rowList.forEach(row -> {
            String[] splitData = row.split("\t");
            String sellerId = splitData[1].trim();
            if (sellerId.length() != 0 && sellerId.length() != 8) {
                System.err.println("賣方統編錯誤: " + sellerId);
                System.exit(-1);
            }
            String invoice = splitData[2];
            String date = splitData[3];
            String description = splitData[4];
            String price = splitData[5].split("\\.")[0];

            finalList.add("document.getElementsByClassName('btn-02-blue btn-left')[2].click();"); // 新增
            finalList.add("document.getElementsByClassName('dropdown-menu inner')[13].getElementsByTagName('li')[2].getElementsByTagName('a')[0].click();"); // 請款大類 (團康獎勵金及忘年餐會)
            finalList.add("document.getElementsByClassName('dropdown-menu inner')[14].getElementsByTagName('li')[2].getElementsByTagName('a')[0].click();"); // 請款中類 (一般團康)
            finalList.add("document.getElementsByClassName('dropdown-menu inner')[15].getElementsByTagName('li')[0].getElementsByTagName('a')[0].click();"); // 費用屬性 (一般)
            finalList.add(String.format("document.getElementById('InvoiceExpenseDesc').value = '%s';", description)); // 請款說明
            if ("".equals(invoice)) {
                finalList.add("document.getElementsByClassName('dropdown-menu inner')[20].getElementsByTagName('li')[1].getElementsByTagName('a')[0].click();"); // 憑證類別 (收據)
            } else {
                finalList.add("document.getElementsByClassName('dropdown-menu inner')[20].getElementsByTagName('li')[0].getElementsByTagName('a')[0].click();"); // 憑證類別 (發票)
            }
            finalList.add(String.format("document.getElementById('TaxIdNum').value = '%s';", sellerId)); // 賣方統編
            finalList.add(String.format("document.getElementById('CertificateNum').value = '%s';", invoice)); // 發票號碼
            finalList.add(String.format("document.getElementById('EstimateVoucherDate').value = '%s';", getCorrectDateFormat(date))); // 憑證日期
            finalList.add(String.format("document.getElementById('InvoiceOriginalAmount').value = '%s';", price)); // 金額
            try {
                finalList.add(String.format("document.getElementById('InvoiceOriginalTax').value = '%s';", splitData[6])); // 稅額
            } catch (Exception e) {
                finalList.add("document.getElementById('InvoiceOriginalTax').value = '0';"); // 稅額
            }
            finalList.add(String.format("document.getElementsByClassName('input h30')[2].value = '%s';", price)); // 分攤金額
            finalList.add("document.getElementById('popConfirm').click();"); // 完成
            finalList.add(String.format("console.log('M %s, %s');\n", splitData[0], splitData[4])); // 完成

        });

        finalList.forEach(System.out::println);

    }

    private String getCorrectDateFormat(String date) {

        String returnDate = null;

        if (date.contains("月") && date.contains("日")) {
            // 格式 => 12月31日
            Integer month = Integer.parseInt(date.split("月")[0]);
            Integer day = Integer.parseInt(date.split("月")[1].split("日")[0]);
            returnDate = String.format("2020-%02d-%02d", month, day);
        } else if (date.length() == 8 && !date.contains("-") && !date.contains("/")) {
            // 格式 => 20200831
            returnDate = String.format("2020-%s-%s", date.substring(4, 6), date.substring(6, 8));
        } else if (date.contains("/")) {
            date = date.replaceAll("/", "-");
            if (date.contains("2020")) {
                // 格式 => 2020/08/14
                Integer month = Integer.parseInt(date.split("-")[1]);
                Integer day = Integer.parseInt(date.split("-")[2]);
                returnDate = String.format("2020-%02d-%02d", month, day);
            } else {
                // 格式 => 8/14
                Integer month = Integer.parseInt(date.split("-")[0]);
                Integer day = Integer.parseInt(date.split("-")[1]);
                returnDate = String.format("2020-%02d-%02d", month, day);
            }
        } else if (date.contains("-")) {
            if (date.contains("2020")) {
                // 格式 => 2020-08-14
                Integer month = Integer.parseInt(date.split("-")[1]);
                Integer day = Integer.parseInt(date.split("-")[2]);
                returnDate = String.format("2020-%02d-%02d", month, day);
            } else {
                // 格式 => 8-14
                Integer month = Integer.parseInt(date.split("-")[0]);
                Integer day = Integer.parseInt(date.split("-")[1]);
                returnDate = String.format("2020-%02d-%02d", month, day);
            }
        } else {
            System.err.println("日期錯誤: " + date);
            System.exit(-1);
        }

        return returnDate;

    }

}
