/** Основний клас програми
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

    public void deleteProductGroup(ProductGroup group) {
        if (groups.remove(group)) {
            group.clearProducts();

            File groupFile = new File(group.name + ".txt");

            if (groupFile.exists()) {
                groupFile.delete();
            }
        }

    }

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

    public void findProductByManufacturer(String manufacturerName) {
        boolean productFound = false;
        System.out.println("Знайдені товари:");
        for (ProductGroup group : groups) {
            for (Product product : group.products) {
                if (product.manufacturer.equalsIgnoreCase(manufacturerName)) {
                    System.out.println(product.name + " - " + product.description + "\n" +
                            ", Виробник: " + product.manufacturer + "\n" +
                            ", Кількість на складі: " + product.stockQuantity + "\n" +
                            ", Ціна: " + product.price + "\n" + " ");
                    productFound = true;
                }
            }
        }
        if (!productFound) {
            System.out.println("Товарів цього виробника не знайдено.");
        }

    }

    public void findProductByPrice(double price) {
        boolean productFound = false;
        System.out.println("Знайдені товари за ціною: ");
        for (ProductGroup group : groups) {
            for (Product product : group.products) {
                if (product.price == price) {
                    System.out.println(product.name + " - " + product.description + "\n" +
                            ", Виробник: " + product.manufacturer + "\n" +
                            ", Кількість на складі: " + product.stockQuantity + "\n" +
                            ", Ціна: " + product.price + "\n" + " ");
                    productFound = true;
                }
            }
        }
        if (!productFound) {
            System.out.println("Товарів за ціною не знайдено.");
        }
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

    public String fileToString(){
        return name + ";" + description + "\n";
    }


    /** Метод для очищення товарів у групі */
    public void clearProducts() {
        products.clear();
    }

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

    public void edit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String toString() {
        return name;
    }

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

    public String toString(){
        return name;
    }

    public String display() {
        return "Назва: " + name + "\n" + "Опис: " + description + "\n" + "Виробник: " + manufacturer+ "\n" + "Кількість: " + stockQuantity + "\n" + "Ціна: " + price ;
    }

    public int writeOff(int number){
        this.stockQuantity = stockQuantity - number;
        return stockQuantity;
    }

    public int writeIn(int number){
        this.stockQuantity = stockQuantity + number;
        return stockQuantity;
    }
    public String fileToString(){
        return name + ";" + description + ";" + manufacturer + ";" + stockQuantity + ";" + price + "\n";
    }
}


