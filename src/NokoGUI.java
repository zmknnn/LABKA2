import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NokoGUI extends JFrame {
    private JTextField search;
    private JButton searchButton;
    private JPanel menuPanel, mainPanel;
    private JLabel menuLabel;
    private CardLayout cardLayout;
    private JComboBox<String> itemsGroupComboBox,  importComboBox , importProductComboBox;

    static Noko noko;
    static ArrayList<ProductGroup> groups;
    public static void create() {
        groups = new ArrayList<ProductGroup>();
        noko = new Noko(groups);
        ProductGroup newGroup = new ProductGroup("Канцелярія", "", new ArrayList<Product>());
        noko.addProductGroup(newGroup);
        ProductGroup newGroup2 = new ProductGroup("Одяг", "", new ArrayList<Product>());
        noko.addProductGroup(newGroup2);
        ProductGroup newGroup3 = new ProductGroup("Квіти", "", new ArrayList<Product>());
        noko.addProductGroup(newGroup3);
        Product newProduct = new Product("Сукня вечірня", "Червона сукня з мереживом", "Vogue", 5, 1200.0);
        newGroup2.addProduct(newProduct);
        Product product2 = new Product("Штани джинсові", "Класичні сині джинси", "Levis", 10, 1500.0);
        newGroup2.addProduct(product2);
        Product product3 = new Product("Сорочка чоловіча", "Біла сорочка класичного крою", "Arber", 7, 900.0);
        newGroup2.addProduct(product3);
        Product product4 = new Product("Піджак класичний", "Чорний піджак з вовни", "Vogue", 3, 2000.0);
        newGroup2.addProduct(product4);
        Product product5 = new Product("Троянда червона", "Свіжа червона троянда", "Флора", 100, 35.0);
        newGroup3.addProduct(product5);
        Product product6 = new Product("Орхідея біла", "Біла орхідея у горщику", "Флора", 20, 250.0);
        newGroup3.addProduct(product6);
        Product product7 = new Product("Гвоздика рожева", "Рожева гвоздика, свіжі квіти", "Флора", 80, 30.0);
        newGroup3.addProduct(product7);
        Product product8 = new Product("Зошит 96 арк.", "Зошит у клітинку, 96 сторінок", "Школярик", 200, 18.5);
        newGroup.addProduct(product8);
        Product product9 = new Product("Ручка гелева", "Гелева ручка синього кольору", "Pilot", 150, 25.0);
        newGroup.addProduct(product9);
        Product product10 = new Product("Папка для паперів", "Папка формату А4, пластикова", "Delta", 60, 40.0);
        newGroup.addProduct(product10);
        Product product11 = new Product("Блокнот А5", "Блокнот у шкіряному переплеті", "Moleskine", 25, 180.0);
        newGroup.addProduct(product11);

    }

    public NokoGUI() {

        groups = new ArrayList<ProductGroup>();
        noko = new Noko(groups);
        loadGroupsFromFile("Групи.txt");
        for (ProductGroup group : groups) {
            String groupFileName = group.name + ".txt";
            loadProductsFromFile(groupFileName, group);
        }

        this.setTitle("Noko - склад товарів");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(230, 230, 250));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        menuLabel = new JLabel("Меню");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(menuLabel);

        // Метод для додавання пустого простору між елементами
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton searchBtn = new JButton("               Пошук                  ");
        JButton groupsBtn = new JButton("         Групи товарів          ");
        JButton itemsBtn = new JButton("               Товари                ");
        JButton importBtn = new JButton("Надходження/Списання");
        JButton statisticBtn = new JButton("            Статистика             ");

        // Розміщення кнопок по центру їхнього JPanel
        searchBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        groupsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        importBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        statisticBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(searchBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(groupsBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(itemsBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(importBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(statisticBtn);

        add(menuPanel, BorderLayout.WEST);

        // CardLayout - дуже зручний тут, бо в певний момент часу можна обирати яка з декількох панелей видима
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        /** Вкладка з пошуком товарів */
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Пошук"));

        // Верхня частина вкладки пошуку
        JPanel textSearchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        search = new JTextField(40);
        searchButton = new JButton("\uD83D\uDD0D");
        textSearchPanel.add(search);
        textSearchPanel.add(searchButton);
        searchPanel.add(textSearchPanel);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel filterLabel = new JLabel("Шукати за:");

        JRadioButton nameBtn = new JRadioButton("Назва");
        JRadioButton manufacturerBtn = new JRadioButton("Виробник");
        //JRadioButton priceBtn = new JRadioButton("Ціна");

        ButtonGroup group = new ButtonGroup();
        group.add(nameBtn);
        group.add(manufacturerBtn);

        filterPanel.add(filterLabel);
        filterPanel.add(nameBtn);
        filterPanel.add(manufacturerBtn);

        searchPanel.add(filterPanel);

        JTextArea searchResultsArea = new JTextArea(7, 50);
        searchResultsArea.setEditable(false);
        searchResultsArea.setLineWrap(true);
        searchResultsArea.setWrapStyleWord(true);
        searchResultsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane searchScrollPane = new JScrollPane(searchResultsArea);

        searchScrollPane.setVisible(false);
        searchScrollPane.setPreferredSize(new Dimension(500, 500));
        searchPanel.add(searchScrollPane);
        searchButton.addActionListener(e -> {
            String query = search.getText().trim();
            if (query.isEmpty()) {
                searchResultsArea.setText("Введіть текст для пошуку.");
                searchScrollPane.setVisible(true);
                return;
            }

            String result = "";

            if (nameBtn.isSelected()) {
                result = Noko.findProductByNamePartial(query);
            } else if (manufacturerBtn.isSelected()) {
                result = Noko.findProductByManufacturerPartial(query);
            } else {
                result = "Будь ласка, оберіть критерій пошуку.";
            }

            searchResultsArea.setText(result);
            searchScrollPane.setVisible(true);

            searchPanel.revalidate();
            searchPanel.repaint();
        });


        /** Вкладка для роботи з групами товарів */
        JPanel groupsPanel = new JPanel();
        groupsPanel.setLayout(new BoxLayout(groupsPanel, BoxLayout.Y_AXIS));
        JLabel groupLabel = new JLabel("Робота з групами товарів");
        groupLabel.setFont(new Font("Arial", Font.BOLD, 18));
        groupLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        groupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        groupsPanel.add(groupLabel);

        groupsPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel groupsAreaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        DefaultListModel<ProductGroup> groupsModel = new DefaultListModel<>();

        JList<ProductGroup> groupsList = new JList<>(groupsModel);
        groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (ProductGroup groupр : groups) {
            groupsModel.addElement(groupр);
        }

        JScrollPane groupsScrollPane = new JScrollPane(groupsList);
        groupsScrollPane.setPreferredSize(new Dimension(270, 305));
        groupsAreaPanel.add(groupsScrollPane);

        JTextArea groupsTextArea = new JTextArea(15, 20);
        groupsTextArea.setEditable(false);
        groupsTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        groupsAreaPanel.add(groupsTextArea);
        groupsPanel.add(groupsAreaPanel);

        groupsPanel.add(groupsAreaPanel);

        groupsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ProductGroup selectedGroup = groupsList.getSelectedValue();
                if (selectedGroup != null) {
                    groupsTextArea.setText(selectedGroup.display());
                }
            }
        });

        JPanel groupsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton groupsAddButton = new JButton("Додати");

        groupsAddButton.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField descriptionField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Назва групи товарів:"));
            panel.add(nameField);
            panel.add(new JLabel("Опис групи товарів:"));
            panel.add(descriptionField);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Додати нову групу товарів",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String description = descriptionField.getText();

                if (!name.isEmpty() && !description.isEmpty()) {

                    ProductGroup newGroup = new ProductGroup(name, description, new ArrayList<Product>());

                    noko.addProductGroup(newGroup);

                    groupsModel.addElement(newGroup);

                    JOptionPane.showMessageDialog(null, "Групу товарів успішно додано!");
                } else {
                    JOptionPane.showMessageDialog(null, "Будь ласка, заповніть всі поля коректно.");
                }
            }
            itemsGroupComboBox.removeAllItems();
            fillComboBoxWithGroups(itemsGroupComboBox, groups);
            importComboBox.removeAllItems();
            fillComboBoxWithGroups(importComboBox, groups);
        });

        JButton groupsEditButton = new JButton("Редагувати");

        groupsEditButton.addActionListener(e -> {
            ProductGroup selectedGroup = groupsList.getSelectedValue();

            if (selectedGroup == null) {
                JOptionPane.showMessageDialog(null, "Будь ласка, оберіть групу для редагування.");
                return;
            }

            JTextField nameField = new JTextField(selectedGroup.name);
            JTextField descriptionField = new JTextField(selectedGroup.description);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Назва групи товарів:"));
            panel.add(nameField);
            panel.add(new JLabel("Опис групи товарів:"));
            panel.add(descriptionField);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Редагувати групу товарів",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String newName = nameField.getText();
                String newDescription = descriptionField.getText();

                if (!newName.isEmpty() && !newDescription.isEmpty()) {
                    selectedGroup.name = newName;
                    selectedGroup.description = newDescription;

                    groupsModel.setElementAt(selectedGroup, groupsList.getSelectedIndex());

                    JOptionPane.showMessageDialog(null, "Групу товарів успішно відредаговано!");
                } else {
                    JOptionPane.showMessageDialog(null, "Будь ласка, заповніть усі поля коректно.");
                }
            }
            groupsTextArea.setText(selectedGroup.display());
        });

        JButton groupsDeleteButton = new JButton("Видалити");

        groupsDeleteButton.addActionListener(e -> {
            ProductGroup selectedGroup = groupsList.getSelectedValue();

            if (selectedGroup == null) {
                JOptionPane.showMessageDialog(null, "Будь ласка, оберіть групу для видалення.");
                return;
            }

            int confirmation = JOptionPane.showConfirmDialog(
                    null,
                    "Ви дійсно хочете видалити цю групу товарів?",
                    "Підтвердження видалення",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                noko.deleteProductGroup(selectedGroup);
                groupsModel.removeElement(selectedGroup);

                JOptionPane.showMessageDialog(null, "Групу товарів успішно видалено!");
            }
            groupsTextArea.setText("");
            itemsGroupComboBox.removeAllItems();
            fillComboBoxWithGroups(itemsGroupComboBox, groups);
            importComboBox.removeAllItems();
            fillComboBoxWithGroups(importComboBox, groups);

        });

        groupsButtonPanel.add(groupsAddButton);
        groupsButtonPanel.add(groupsEditButton);
        groupsButtonPanel.add(groupsDeleteButton);

        groupsPanel.add(groupsButtonPanel);


        /** Вкладка для роботи з товарами */
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        JLabel itemsLabel = new JLabel("Робота з товарами");
        itemsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        itemsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        itemsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemsPanel.add(itemsLabel);

        JPanel itemsGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel selectItemsGroupLabel = new JLabel("Виберіть групу товарів : ");

        itemsGroupComboBox = new JComboBox<>();
        fillComboBoxWithGroups(itemsGroupComboBox, groups);

        itemsGroupPanel.add(selectItemsGroupLabel);
        itemsGroupPanel.add(itemsGroupComboBox);

        itemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        itemsPanel.add(itemsGroupPanel);

        JPanel itemsProductPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        DefaultListModel<Product> productsModel = new DefaultListModel<>();

        JList<Product> productsList = new JList<>(productsModel);
        productsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(productsList);
        scrollPane.setPreferredSize(new Dimension(275, 300));
        itemsProductPanel.add(scrollPane);

        itemsGroupComboBox.addActionListener(e -> {
            String groupName = (String) itemsGroupComboBox.getSelectedItem();
            ProductGroup selectedGroup = findGroupByName(groupName, groups);
            productsModel.clear();
            if (selectedGroup != null) {
                for (Product product : selectedGroup.products) {
                    productsModel.addElement(product);
                }
            }
        });

        JTextArea itemsTextArea = new JTextArea(17, 20);
        itemsTextArea.setEditable(false);
        itemsTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        itemsProductPanel.add(itemsTextArea);
        itemsPanel.add(itemsProductPanel);

        productsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Product selectedProduct = productsList.getSelectedValue();
                if (selectedProduct != null) {
                    itemsTextArea.setText(selectedProduct.display());
                }
            }
        });

        JPanel itemsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton itemsAddButton = new JButton("Додати");

        itemsAddButton.addActionListener(e -> {
            boolean success = false;

            // Зберігаємо введене між повторними спробами
            JTextField nameField = new JTextField();
            JTextField descriptionField = new JTextField();
            JTextField manufacturerField = new JTextField();
            JTextField quantityField = new JTextField();
            JTextField priceField = new JTextField();

            while (!success) {
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Назва товару:"));
                panel.add(nameField);
                panel.add(new JLabel("Опис товару:"));
                panel.add(descriptionField);
                panel.add(new JLabel("Виробник:"));
                panel.add(manufacturerField);
                panel.add(new JLabel("Кількість:"));
                panel.add(quantityField);
                panel.add(new JLabel("Ціна:"));
                panel.add(priceField);

                int result = JOptionPane.showConfirmDialog(
                        null,
                        panel,
                        "Додати новий товар",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result != JOptionPane.OK_OPTION) {
                    break;
                }

                try {
                    String name = nameField.getText().trim();
                    String description = descriptionField.getText().trim();
                    String manufacturer = manufacturerField.getText().trim();
                    String quantityStr = quantityField.getText().trim();
                    String priceStr = priceField.getText().trim();

                    if (name.isEmpty() || description.isEmpty() || manufacturer.isEmpty() ||
                            quantityStr.isEmpty() || priceStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Будь ласка, заповніть усі поля.");
                        continue;
                    }

                    if (!isNameUnique(name, groups)) {
                        JOptionPane.showMessageDialog(null, "Така назва товару або групи вже існує.");
                        continue;
                    }

                    int quantity = Integer.parseInt(quantityStr);
                    double price = Double.parseDouble(priceStr);

                    if (quantity < 0 || price < 0) {
                        JOptionPane.showMessageDialog(null, "Кількість та ціна не можуть бути від’ємними.");
                        continue;
                    }

                    String groupName = (String) itemsGroupComboBox.getSelectedItem();
                    ProductGroup selectedGroup = findGroupByName(groupName, groups);

                    if (selectedGroup != null) {
                        Product newProduct = new Product(name, description, manufacturer, quantity, price);
                        selectedGroup.addProduct(newProduct);
                        productsModel.addElement(newProduct);

                        JOptionPane.showMessageDialog(null, "Товар успішно додано!");
                        success = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Групу не знайдено.");
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Кількість та ціна мають бути числовими значеннями.");
                }
            }
        });

        JButton itemsEditButton = new JButton("Редагувати");

        itemsEditButton.addActionListener(e -> {
            Product selectedProduct = productsList.getSelectedValue();

            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(null, "Будь ласка, оберіть товар для редагування.");
                return;
            }

            JTextField nameField = new JTextField(selectedProduct.name);
            JTextField descriptionField = new JTextField(selectedProduct.description);
            JTextField manufacturerField = new JTextField(selectedProduct.manufacturer);
            JTextField quantityField = new JTextField(String.valueOf(selectedProduct.stockQuantity));
            JTextField priceField = new JTextField(String.valueOf(selectedProduct.price));

            boolean success = false;

            while (!success) {
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Назва товару:"));
                panel.add(nameField);
                panel.add(new JLabel("Опис товару:"));
                panel.add(descriptionField);
                panel.add(new JLabel("Виробник:"));
                panel.add(manufacturerField);
                panel.add(new JLabel("Кількість:"));
                panel.add(quantityField);
                panel.add(new JLabel("Ціна:"));
                panel.add(priceField);

                int result = JOptionPane.showConfirmDialog(
                        null,
                        panel,
                        "Редагувати товар",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result != JOptionPane.OK_OPTION) {
                    break;
                }

                try {
                    String newName = nameField.getText().trim();
                    String newDescription = descriptionField.getText().trim();
                    String newManufacturer = manufacturerField.getText().trim();
                    String quantityStr = quantityField.getText().trim();
                    String priceStr = priceField.getText().trim();

                    if (newName.isEmpty() || newDescription.isEmpty() || newManufacturer.isEmpty() ||
                            quantityStr.isEmpty() || priceStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Будь ласка, заповніть усі поля.");
                        continue;
                    }

                    if (!newName.equalsIgnoreCase(selectedProduct.name) && !isNameUnique(newName, groups)) {
                        JOptionPane.showMessageDialog(null, "Така назва товару або групи вже існує.");
                        continue;
                    }

                    int newQuantity = Integer.parseInt(quantityStr);
                    double newPrice = Double.parseDouble(priceStr);

                    if (newQuantity < 0 || newPrice < 0) {
                        JOptionPane.showMessageDialog(null, "Кількість і ціна не можуть бути від’ємними.");
                        continue;
                    }

                    // Оновлення товару
                    selectedProduct.name = newName;
                    selectedProduct.description = newDescription;
                    selectedProduct.manufacturer = newManufacturer;
                    selectedProduct.stockQuantity = newQuantity;
                    selectedProduct.price = newPrice;

                    itemsTextArea.setText(selectedProduct.display());
                    productsList.repaint();

                    JOptionPane.showMessageDialog(null, "Товар успішно відредаговано!");
                    success = true;

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Кількість та ціна мають бути числовими значеннями.");
                }
            }
        });

        JButton itemsDeleteButton = new JButton("Видалити");

        itemsDeleteButton.addActionListener(e -> {
            Product selectedProduct = productsList.getSelectedValue();

            if (selectedProduct != null) {
                int confirmation = JOptionPane.showConfirmDialog(
                        null,
                        "Ви дійсно хочете видалити товар: " + selectedProduct.name + "?",
                        "Підтвердження видалення",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirmation == JOptionPane.YES_OPTION) {
                    String groupName = (String) itemsGroupComboBox.getSelectedItem();
                    ProductGroup selectedGroup = findGroupByName(groupName, groups);

                    if (selectedGroup != null) {
                        selectedGroup.deleteProduct(selectedProduct);

                        productsModel.removeElement(selectedProduct);
                        itemsTextArea.setText(""); // Очищаємо відображення

                        JOptionPane.showMessageDialog(null, "Товар успішно видалено!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Видалення скасовано.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Будь ласка, виберіть товар для видалення.");
            }
        });

        itemsButtonPanel.add(itemsAddButton);
        itemsButtonPanel.add(itemsEditButton);
        itemsButtonPanel.add(itemsDeleteButton);

        itemsPanel.add(itemsButtonPanel);


        /** Вкладка для надходження/списання */
        JPanel importPanel = new JPanel();
        importPanel.setLayout(new BoxLayout(importPanel, BoxLayout.Y_AXIS));
        JLabel importLabel = new JLabel("Надходження/Списання");
        importLabel.setFont(new Font("Arial", Font.BOLD, 18));
        importLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        importLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        importPanel.add(importLabel);

        JPanel choice1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel selectImportLabel = new JLabel("Виберіть групу товарів : ");

        importComboBox = new JComboBox<>();
        fillComboBoxWithGroups(importComboBox, groups);

        choice1Panel.add(selectImportLabel);
        choice1Panel.add(importComboBox);

        importPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        importPanel.add(choice1Panel);

        importProductComboBox = new JComboBox<>();

        JPanel choice2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        choice2Panel.add(new JLabel("Виберіть товар: "));
        choice2Panel.add(importProductComboBox);

        importComboBox.addActionListener(e -> {
            String groupName = (String) importComboBox.getSelectedItem();
            ProductGroup selectedGroup = findGroupByName(groupName, groups);
            importProductComboBox.removeAllItems();
            if (selectedGroup != null) {
                fillComboBoxWithProducts(importProductComboBox, selectedGroup.products);
            }
        });

        String initialGroupName = (String) importComboBox.getSelectedItem();
        ProductGroup initialGroup = findGroupByName(initialGroupName, groups);
        if (initialGroup != null) {
            fillComboBoxWithProducts(importProductComboBox, initialGroup.products);
        }

        importPanel.add(choice2Panel);

        JPanel informationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextArea informationArea = new JTextArea(28, 35);
        informationArea.setEditable(false);
        informationArea.setLineWrap(true);
        informationArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        informationPanel.add(informationArea);
        importPanel.add(informationPanel);

        importProductComboBox.addActionListener(e -> {
            String productName = (String) importProductComboBox.getSelectedItem();
            String groupName = (String) importComboBox.getSelectedItem();
            ProductGroup selectedGroup = findGroupByName(groupName, groups);

            if (selectedGroup != null && productName != null) {
                Product selectedProduct = findProductByName(productName, selectedGroup.products);
                if (selectedProduct != null) {
                    informationArea.setText(selectedProduct.display());
                } else {
                    informationArea.setText("Товар не знайдено.");
                }
            }
        });

        JPanel choice3Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton writeInButton = new JButton("Додати");

        writeInButton.addActionListener(e -> {
            String productName = (String) importProductComboBox.getSelectedItem();
            String groupName = (String) importComboBox.getSelectedItem();
            ProductGroup selectedGroup = findGroupByName(groupName, groups);
            Product selectedProduct = findProductByName(productName, selectedGroup.products);

            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(null, "Товар не знайдено.");
                return;
            }

            JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            JTextField quantityWriteInField = new JTextField(5);
            JLabel label = new JLabel("Введіть кількість для надходження:");

            inputPanel.add(label);
            inputPanel.add(quantityWriteInField);

            Object[] options = {"Додати", "Відмінити"};

            int option = JOptionPane.showOptionDialog(
                    null,
                    inputPanel,
                    "Надходження товару",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (option == 0) {
                try {
                    int quantity = Integer.parseInt(quantityWriteInField.getText());
                    if (quantity > 0) {
                        selectedProduct.writeIn(quantity);
                        JOptionPane.showMessageDialog(null, "Товар успішно додано!");
                        informationArea.setText(selectedProduct.display());
                    } else {
                        JOptionPane.showMessageDialog(null, "Кількість має бути більшою за нуль.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Будь ласка, введіть коректну кількість.");
                }
            }
        });

        choice3Panel.add(writeInButton);
        JButton writeOffButton = new JButton("Списати");

        writeOffButton.addActionListener(e -> {

            String productName = (String) importProductComboBox.getSelectedItem();
            String groupName = (String) importComboBox.getSelectedItem();
            ProductGroup selectedGroup = findGroupByName(groupName, groups);
            Product selectedProduct = findProductByName(productName, selectedGroup.products);

            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(null, "Товар не знайдено.");
                return;
            }

            String[] options = {"Списати", "Відмінити"};

            JSlider writeOffSlider = new JSlider(JSlider.HORIZONTAL,0 , selectedProduct.stockQuantity, 0);
            int maxTickSpacing = 1;
            if (selectedProduct.stockQuantity > 10 && selectedProduct.stockQuantity <= 50) {
                maxTickSpacing = 5;
            } else if (selectedProduct.stockQuantity > 50 && selectedProduct.stockQuantity <= 100) {
                maxTickSpacing = 10;
            } else if (selectedProduct.stockQuantity > 100) {
                maxTickSpacing = 20;
            }

            writeOffSlider.setMajorTickSpacing(maxTickSpacing);
            writeOffSlider.setPaintTicks(true);
            writeOffSlider.setPaintLabels(true);

            JTextField quantityWriteOffField = new JTextField(5);
            quantityWriteOffField.setText(String.valueOf(writeOffSlider.getValue()));
            quantityWriteOffField.setEditable(false);

            JPanel sliderPanel = new JPanel();
            sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
            sliderPanel.add(new JLabel("Виберіть кількість для списання:"));
            sliderPanel.add(writeOffSlider);
            sliderPanel.add(quantityWriteOffField);

            writeOffSlider.addChangeListener(r -> {
                quantityWriteOffField.setText(String.valueOf(writeOffSlider.getValue()));
            });

            int choice = JOptionPane.showOptionDialog(
                    null,
                    sliderPanel,
                    "Списання товару",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                int selectedValue = writeOffSlider.getValue();
                selectedProduct.writeOff(selectedValue);
            }

            informationArea.setText(selectedProduct.display());
        });

        choice3Panel.add(writeOffButton);

        importPanel.add(choice3Panel);


        /** Вкладка для статистики */
        JPanel statisticPanel = new JPanel();
        statisticPanel.add(new JLabel("Статистика"));

        // --- Статистика ---
        statisticPanel.removeAll();
        statisticPanel.setLayout(new BorderLayout());

        JLabel statisticHeader = new JLabel("СТАТИСТИКА");
        statisticHeader.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel headerPanel = new JPanel();
        headerPanel.add(statisticHeader);
        statisticPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        JButton allProductsBtn = new JButton("Вся продукція");
        JButton byGroupsBtn = new JButton("По групах");
        JButton totalValueBtn = new JButton("Загальна вартість складу");
        tabsPanel.add(allProductsBtn);
        tabsPanel.add(byGroupsBtn);
        tabsPanel.add(totalValueBtn);
        statisticPanel.add(tabsPanel, BorderLayout.CENTER);

        JPanel tabContentPanel = new JPanel(new CardLayout());

        String[] columns = {"Назва", "Група", "Кількість", "Ціна", "Загальна вартість"};

        JTable allProductsTable = new JTable(new Object[0][columns.length], columns);
        JScrollPane scrollAll = new JScrollPane(allProductsTable);
        JPanel allProductsPanel = new JPanel(new BorderLayout());
        allProductsPanel.setBorder(BorderFactory.createTitledBorder("Вся продукція"));
        allProductsPanel.add(scrollAll, BorderLayout.CENTER);

        fillAllProductsTable(allProductsTable, groups);

        JComboBox<String> group2ComboBox = new JComboBox<>();
        fillComboBoxWithGroups(group2ComboBox, groups);

        JTable groupTable = new JTable(new Object[0][columns.length], columns);
        JScrollPane scrollGroup = new JScrollPane(groupTable);
        JLabel groupTotalLabel = new JLabel("Загальна вартість групи: 0");

        group2ComboBox.addActionListener(e -> {
            String selectedGroupName = (String) group2ComboBox.getSelectedItem();
            if (selectedGroupName != null) {
                for (ProductGroup grooup : groups) {
                    if (grooup.name.equals(selectedGroupName)) {
                        fillGroupTable(groupTable, grooup, groupTotalLabel);
                        break;
                    }
                }
            }
        });

        JPanel byGroupsPanel = new JPanel(new BorderLayout());
        byGroupsPanel.setBorder(BorderFactory.createTitledBorder("По групах"));
        byGroupsPanel.add(group2ComboBox, BorderLayout.NORTH);
        byGroupsPanel.add(scrollGroup, BorderLayout.CENTER);
        byGroupsPanel.add(groupTotalLabel, BorderLayout.SOUTH);

        /** ДОДАНО */
        double totalPrice = 0.0;

        for (ProductGroup ggroup : groups) {
            for (Product product : ggroup.products) {
                totalPrice += product.price * product.stockQuantity;
            }
        }
        JLabel totalValueLabel = new JLabel("Загальна вартість товарів на складі: " + totalPrice);
        totalValueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel totalValuePanel = new JPanel(new BorderLayout());
        totalValuePanel.setBorder(BorderFactory.createTitledBorder("Загальна вартість складу"));
        totalValuePanel.add(totalValueLabel, BorderLayout.CENTER);

        tabContentPanel.add(allProductsPanel, "allProducts");
        tabContentPanel.add(byGroupsPanel, "byGroups");
        tabContentPanel.add(totalValuePanel, "totalValue");

        statisticPanel.add(tabContentPanel, BorderLayout.SOUTH);

        CardLayout statisticCardLayout = (CardLayout) tabContentPanel.getLayout();

        allProductsBtn.addActionListener(e -> statisticCardLayout.show(tabContentPanel, "allProducts"));
        byGroupsBtn.addActionListener(e -> statisticCardLayout.show(tabContentPanel, "byGroups"));
        totalValueBtn.addActionListener(e -> statisticCardLayout.show(tabContentPanel, "totalValue"));

        mainPanel.add(searchPanel, "search");
        mainPanel.add(groupsPanel, "groups");
        mainPanel.add(itemsPanel, "items");
        mainPanel.add(importPanel, "import");
        mainPanel.add(statisticPanel, "statistic");

        add(mainPanel, BorderLayout.CENTER);

        /** Перемикання між вкладками */
        searchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "search");
            }
        });
        groupsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "groups");
            }
        });
        itemsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "items");
            }
        });
        importBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "import");
            }
        });
        statisticBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "statistic");
            }
        });

    }

    public void fillComboBoxWithGroups(JComboBox<String> comboBox, ArrayList<ProductGroup> groups) {
        for (ProductGroup group : groups) {
            comboBox.addItem(group.name);
        }
    }

    public void fillComboBoxWithProducts(JComboBox<String> comboBox, ArrayList<Product> products) {
        for (Product product : products) {
            comboBox.addItem(product.name);
        }
    }

    public ProductGroup findGroupByName(String name, ArrayList<ProductGroup> groups) {
        for (ProductGroup group : groups) {
            if (group.name.equalsIgnoreCase(name)) {
                return group;
            }
        }
        return null;
    }

    public Product findProductByName(String name, ArrayList<Product> products) {
        for (Product product : products) {
            if (product.name.equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    public void fillGroupTable(JTable table, ProductGroup group, JLabel totalLabel) {
        String[] columns = {"Назва", "Виробник", "Ціна", "Кількість", "Опис"};
        List<Product> products = group.products;
        Object[][] data = new Object[products.size()][columns.length];
        double total = 0;

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            data[i][0] = p.name;
            data[i][1] = p.manufacturer;
            data[i][2] = p.price;
            data[i][3] = p.stockQuantity;
            data[i][4] = p.description;
            total += p.price * p.stockQuantity;
        }

        table.setModel(new DefaultTableModel(data, columns));
        totalLabel.setText("Загальна вартість групи: " + total);
    }

    public void fillAllProductsTable(JTable table, List<ProductGroup> groups) {
        String[] columns = {"Назва", "Група", "Кількість", "Ціна", "Загальна вартість"};
        List<Object[]> rows = new ArrayList<>();

        for (ProductGroup group : groups) {
            for (Product product : group.products) {
                Object[] row = {
                        product.name,
                        group.name,
                        product.stockQuantity,
                        product.price,
                        product.stockQuantity * product.price
                };
                rows.add(row);
            }
        }

        Object[][] data = rows.toArray(new Object[0][]);
        table.setModel(new DefaultTableModel(data, columns));
    }

    public boolean isNameUnique(String nameToCheck, ArrayList<ProductGroup> groups) {
        for (ProductGroup group : groups) {
            if (group.name.equalsIgnoreCase(nameToCheck)) {
                return false;
            }
            for (Product product : group.products) {
                if (product.name.equalsIgnoreCase(nameToCheck)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Product StringToProduct(String fileString) {
        String[] parts = fileString.split(";");

        String name = parts[0];
        String description = parts[1];
        String manufacturer = parts[2];
        int stockQuantity = Integer.parseInt(parts[3]);
        double price = Double.parseDouble(parts[4]);

        return new Product(name, description, manufacturer, stockQuantity, price);
    }

    public ProductGroup StringToProductGroup(String fileString) {
        String[] parts = fileString.split(";");

        String name = parts[0];
        String description = parts[1];

        return new ProductGroup(name, description, new ArrayList<Product>());
    }

    public void loadGroupsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ProductGroup newProductGroup = StringToProductGroup(line);
                groups.add(newProductGroup);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProductsFromFile(String filePath, ProductGroup group) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product newProduct = StringToProduct(line);
                group.products.add(newProduct);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        NokoGUI noko = new NokoGUI();
        noko.setVisible(true);
    }
}
