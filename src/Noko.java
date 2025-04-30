
/** Клас зі структурою і методами для основної програми
 *
 * @author Кузь Христина
 * @author Котульська Нонна
 * */

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/** Клас, який представляє підприємство */
public class Noko {
    String name;
    static ArrayList <ProductGroup> groups;

    public Noko(ArrayList<ProductGroup> groups) {
        this.name = "Noko";
        this.groups = groups;
    }

    /** Метод для додання групи товарів */
    public void addProductGroup(ProductGroup newGroup) {
        for (ProductGroup group : groups) {
            if (group.name.equalsIgnoreCase(newGroup.name)) {
                System.out.println("Група з такою назвою існує.");
                return;
            }
        }
        groups.add(newGroup);
        File groupFile = new File(newGroup.name + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(groupFile))) {
            for (Product product : newGroup.products) {
                writer.write(product.fileToString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Помилка при створенні або записі файлу: " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Групи.txt", true))) {
            writer.append(newGroup.fileToString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Помилка при створенні або записі файлу: " + e.getMessage());
        }
    }

    /** Метод для видалення групи товарів */
    public void deleteProductGroup(ProductGroup group) {
        if (groups.remove(group)) {
            group.clearProducts();

            File groupFile = new File(group.name + ".txt");

            if (groupFile.exists()) {
                groupFile.delete();
            }
        }

    }

    /** Метод для редагування товарів */
    public void editProductGroup(ProductGroup group, String newName, String newDescription) {
        String oldName = group.name;
        String oldFileName = oldName + ".txt";
        String newFileName = newName + ".txt";

        group.name = newName;
        group.description = newDescription;

        if (!oldName.equals(newName)) {
            File oldFile = new File(oldFileName);
            File newFile = new File(newFileName);
            if (oldFile.exists()) {
                oldFile.renameTo(newFile);
            }
        }

        File groupsFile = new File("Групи.txt");
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(groupsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(oldName + ";")) {
                    lines.add(group.fileToString());
                } else {
                    lines.add(line);
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(groupsFile));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Метод для знаходження товару за назвою (початком) */
    public static String findProductByNamePartial(String query) {
        boolean productFound = false;
        StringBuilder result = new StringBuilder("");

        if (groups != null) {
            for (ProductGroup group : groups) {
                if (group != null && group.products != null) {
                    for (Product product : group.products) {
                        if (product != null && product.name.toLowerCase().startsWith(query.toLowerCase())) {
                            result.append(product.name)
                                    .append(" : ")
                                    .append("\n")
                                    .append(product.description)
                                    .append(", Виробник: ")
                                    .append(product.manufacturer)
                                    .append(", Кількість на складі: ")
                                    .append(product.stockQuantity)
                                    .append(", Ціна: ")
                                    .append(product.price)
                                    .append("\n")
                                    .append("\n");
                            productFound = true;
                        }
                    }
                }
            }
        }
        if (!productFound) {
            result.append("Товар не знайдено.");
        }
        return result.toString();
    }

    /** Метод для знаходження товару за виробником (початком) */
    public static String findProductByManufacturerPartial(String query) {
        boolean productFound = false;
        StringBuilder result = new StringBuilder("");

        if (groups != null) {
            for (ProductGroup group : groups) {
                if (group != null && group.products != null) {
                    for (Product product : group.products) {
                        if (product != null && product.manufacturer.toLowerCase().startsWith(query.toLowerCase())) {
                            result.append(product.name)
                                    .append(" : ")
                                    .append("\n")
                                    .append(product.description)
                                    .append(", Виробник: ")
                                    .append(product.manufacturer)
                                    .append(", Кількість на складі: ")
                                    .append(product.stockQuantity)
                                    .append(", Ціна: ")
                                    .append(product.price)
                                    .append("\n")
                                    .append("\n");
                            productFound = true;
                        }
                    }
                }
            }
        }

        if (!productFound) {
            result.append("Товарів цього виробника не знайдено.");
        }
        return result.toString();
    }

}

/** Клас, який представляє групу товарів */
class ProductGroup {
    String name;
    String description;
    ArrayList <Product> products;

    public ProductGroup(String name, String description, ArrayList <Product> products) {
        this.name = name;
        this.products = products;
        this.description = description;
    }

    /** Для запису групи у файл */
    public String fileToString(){
        return name + ";" + description;
    }

    /** Метод для очищення товарів у групі */
    public void clearProducts() {
        products.clear();
    }

    /** Метод для додання товару */
    public void addProduct(Product newProduct) {
        for (Product product : products) {
            if (product.name.equalsIgnoreCase(newProduct.name)) {
                System.out.println("Товар з такою назвою вже існує в цій групі.");
                return;
            }
        }
        products.add(newProduct);

        File groupFile = new File(this.name + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(groupFile, true))) {
            writer.write(newProduct.fileToString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Помилка при запису товару у файл: " + e.getMessage());
        }
    }

    /** Метод для видалення товару */
    public void deleteProduct(Product product) {
        products.remove(product);

        File groupFile = new File(this.name + ".txt");

        try {
            List<String> lines = Files.readAllLines(groupFile.toPath());

            // Створення нового списку без рядка, що містить товар
            List<String> updatedLines = new ArrayList<>();
            for (String line : lines) {
                if (!line.equals(product.toString())) { // Порівнюємо товар з рядком у файлі
                    updatedLines.add(line);
                }
            }

            Files.write(groupFile.toPath(), updatedLines);
        } catch (IOException e) {
            System.out.println("Помилка при видаленні товару з файлу: " + e.getMessage());
        }
    }

    /** Метод, що повертає назву групи */
    public String toString() {
        return name;
    }

    /** Для виводу інформації про групу */
    public String display() {
        return "Назва: " + name + "\n" + "Опис: " + description;
    }

}

/** Клас, який представляє товар */
class Product {
    String name;
    String description;
    String manufacturer;
    int stockQuantity;
    double price;

    public Product(String name, String description, String manufacturer, int stockQuantity, double price) {
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    /** Повертає просто назву товару */
    public String toString(){
        return name;
    }

    /** Виводить інформацію про товар */
    public String display() {
        return "Назва: " + name + "\n" + "Опис: " + description + "\n" + "Виробник: " + manufacturer+ "\n" + "Кількість: " + stockQuantity + "\n" + "Ціна: " + price ;
    }

    /** Для списання товару */
    public int writeOff(int number){
        this.stockQuantity = stockQuantity - number;
        return stockQuantity;
    }

    /** Для надходження товару */
    public int writeIn(int number){
        this.stockQuantity = stockQuantity + number;
        return stockQuantity;
    }

    /** Для записування товару у файл */
    public String fileToString(){
        return name + ";" + description + ";" + manufacturer + ";" + stockQuantity + ";" + price ;
    }

    /** Для редагування товару */
    public void edit(ProductGroup groupName, String newName, String newDescription, String newManufacturer, int newStockQuantity, double newPrice) {
        String oldName = this.name;
        this.name = newName;
        this.description = newDescription;
        this.manufacturer = newManufacturer;
        this.stockQuantity = newStockQuantity;
        this.price = newPrice;

        File groupFile = new File(groupName + ".txt");
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(groupFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(oldName + ";")) {
                    lines.add(this.fileToString());
                } else {
                    lines.add(line);
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(groupFile));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


