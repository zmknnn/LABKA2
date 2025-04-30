import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** */

public class NokoGUI extends JFrame {
    private JTextField search;
    private JButton searchButton;
    private JPanel menuPanel, mainPanel;
    private JLabel menuLabel;
    private CardLayout cardLayout;
    private JComboBox<String> itemsGroupComboBox,  importComboBox , importProductComboBox;

    static Noko noko;
    static ArrayList<ProductGroup> groups;

    /** */
    public NokoGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Panel.background", new Color(232, 243, 255));
            UIManager.put("OptionPane.background", new Color(232, 243, 255));
            UIManager.put("OptionPane.messageForeground", new Color(44, 62, 80));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /** Завантаження груп та товарів з файлів */
        groups = new ArrayList<ProductGroup>();
        noko = new Noko(groups);
        loadGroupsFromFile("Групи.txt");
        for (ProductGroup group : groups) {
            String groupFileName = group.name + ".txt";
            loadProductsFromFile(groupFileName, group);
        }

        /** Налаштування базового вікна */
        this.setTitle("Noko - склад товарів");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        /** Панель меню */
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(198, 221, 255));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        menuLabel = new JLabel("Меню");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(menuLabel);

        // Метод для додавання пустого простору між елементами
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        /** Кнопки меню */
        JButton searchBtn = new JButton("              Пошук                 ");
        searchBtn.setBackground(new Color(160, 197, 248));
        searchBtn.setForeground(new Color(33, 33, 33));
        searchBtn.setOpaque(true);
        searchBtn.setBorderPainted(false);
        searchBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton groupsBtn = new JButton("         Групи товарів          ");
        groupsBtn.setBackground(new Color(160, 197, 248));
        groupsBtn.setForeground(new Color(33, 33, 33));
        groupsBtn.setOpaque(true);
        groupsBtn.setBorderPainted(false);
        groupsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton itemsBtn = new JButton("               Товари                ");
        itemsBtn.setBackground(new Color(160, 197, 248));
        itemsBtn.setForeground(new Color(33, 33, 33));
        itemsBtn.setOpaque(true);
        itemsBtn.setBorderPainted(false);
        itemsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton importBtn = new JButton("  Надходження/Списання  ");
        importBtn.setBackground(new Color(160, 197, 248));
        importBtn.setForeground(new Color(33, 33, 33));
        importBtn.setOpaque(true);
        importBtn.setBorderPainted(false);
        importBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton statisticBtn = new JButton("            Статистика             ");
        statisticBtn.setBackground(new Color(160, 197, 248));
        statisticBtn.setForeground(new Color(33, 33, 33));
        statisticBtn.setOpaque(true);
        statisticBtn.setBorderPainted(false);
        statisticBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));

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

        // Текстове поле і кнопка
        search = new JTextField(40);
        searchButton = new JButton("\uD83D\uDD0D");
        searchButton.setBackground(new Color(191, 214, 246));
        searchButton.setForeground(new Color(33, 33, 33));
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        textSearchPanel.add(search);
        textSearchPanel.add(searchButton);
        searchPanel.add(textSearchPanel);

        // Фільтри за чим здійснювати пошук (використовували JRadioButton)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel filterLabel = new JLabel("Шукати за:");

        JRadioButton nameBtn = new JRadioButton("Назва");
        nameBtn.setBackground(new Color(232, 243, 255));
        nameBtn.setForeground(new Color(33, 33, 33));
        nameBtn.setOpaque(true);
        nameBtn.setBorderPainted(false);
        JRadioButton manufacturerBtn = new JRadioButton("Виробник");
        manufacturerBtn.setBackground(new Color(232, 243, 255));
        manufacturerBtn.setForeground(new Color(33, 33, 33));
        manufacturerBtn.setOpaque(true);
        manufacturerBtn.setBorderPainted(false);
        // Об'єднали їх в групу
        ButtonGroup group = new ButtonGroup();
        group.add(nameBtn);
        group.add(manufacturerBtn);

        filterPanel.add(filterLabel);
        filterPanel.add(nameBtn);
        filterPanel.add(manufacturerBtn);

        searchPanel.add(filterPanel);

        // Вивід результатів пошуку
        JTextArea searchResultsArea = new JTextArea(7, 50);
        searchResultsArea.setBackground(new Color(232, 243, 255));
        searchResultsArea.setEditable(false);
        searchResultsArea.setLineWrap(true);
        searchResultsArea.setWrapStyleWord(true);
        searchResultsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane searchScrollPane = new JScrollPane(searchResultsArea);

        searchScrollPane.setVisible(false);
        searchScrollPane.setPreferredSize(new Dimension(500, 500));
        searchPanel.add(searchScrollPane);

        /** Подія для натискання кнопки пошуку */
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

        // Панель, де відображатимуться групи
        JPanel groupsAreaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // JList груп товарів
        DefaultListModel<ProductGroup> groupsModel = new DefaultListModel<>();
        JList<ProductGroup> groupsList = new JList<>(groupsModel);
        groupsList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupsList.setBackground(new Color(255, 255, 255));

        for (ProductGroup groupр : groups) {
            groupsModel.addElement(groupр);
        }

        JScrollPane groupsScrollPane = new JScrollPane(groupsList);
        groupsScrollPane.setPreferredSize(new Dimension(270, 305));
        groupsAreaPanel.add(groupsScrollPane);
        // Поле для інформації про вибрану групу
        JTextArea groupsTextArea = new JTextArea(15, 20);
        groupsTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        groupsTextArea.setBackground(new Color(255, 255, 255));
        groupsTextArea.setEditable(false);

        groupsAreaPanel.add(groupsTextArea);
        groupsPanel.add(groupsAreaPanel);
        groupsPanel.add(groupsAreaPanel);

        /** Подія для вибору різних елементів JList */
        groupsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ProductGroup selectedGroup = groupsList.getSelectedValue();
                if (selectedGroup != null) {
                    groupsTextArea.setText(selectedGroup.display());
                }
            }
        });

        // Панель з кнопками для роботи з групами
        JPanel groupsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton groupsAddButton = new JButton("Додати");
        groupsAddButton.setBackground(new Color(191, 214, 246));
        groupsAddButton.setForeground(new Color(33, 33, 33));
        groupsAddButton.setOpaque(true);
        groupsAddButton.setBorderPainted(false);
        groupsAddButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        /** Подія від натискання кнопки "Додати" */
        groupsAddButton.addActionListener(e -> {
            boolean success = false;

            JTextField nameField = new JTextField();
            JTextField descriptionField = new JTextField();

            while (!success) {
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

                if (result != JOptionPane.OK_OPTION) {
                    break;
                }

                String name = nameField.getText().trim();
                String description = descriptionField.getText().trim();

                if (name.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Будь ласка, заповніть всі поля коректно.");
                    continue;
                }

                if (!isNameUnique(name, groups)) {
                    JOptionPane.showMessageDialog(null, "Група або товар з такою назвою вже існує.");
                    continue;
                }

                ProductGroup newGroup = new ProductGroup(name, description, new ArrayList<>());
                noko.addProductGroup(newGroup);
                groupsModel.addElement(newGroup);

                JOptionPane.showMessageDialog(null, "Групу товарів успішно додано!");
                success = true;
            }

            itemsGroupComboBox.removeAllItems();
            fillComboBoxWithGroups(itemsGroupComboBox, groups);
            importComboBox.removeAllItems();
            fillComboBoxWithGroups(importComboBox, groups);
        });

        JButton groupsEditButton = new JButton("Редагувати");
        groupsEditButton.setBackground(new Color(191, 214, 246));
        groupsEditButton.setForeground(new Color(33, 33, 33));
        groupsEditButton.setOpaque(true);
        groupsEditButton.setBorderPainted(false);
        groupsEditButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        /** Подія від натискання кнопки "Редагувати" */
        groupsEditButton.addActionListener(e -> {
            ProductGroup selectedGroup = groupsList.getSelectedValue();

            if (selectedGroup == null) {
                JOptionPane.showMessageDialog(null, "Будь ласка, оберіть групу для редагування.");
                return;
            }

            boolean success = false;

            while (!success) {
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

                if (result != JOptionPane.OK_OPTION) {
                    break;
                }

                String newName = nameField.getText().trim();
                String newDescription = descriptionField.getText().trim();

                if (newName.isEmpty() || newDescription.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Будь ласка, заповніть усі поля.");
                    continue;
                }

                if (!newName.equalsIgnoreCase(selectedGroup.name) && !isNameUnique(newName, groups)) {
                    JOptionPane.showMessageDialog(null, "Група або товар з такою назвою вже існує.");
                    continue;
                }

                noko.editProductGroup(selectedGroup, newName, newDescription);
                groupsModel.setElementAt(selectedGroup, groupsList.getSelectedIndex());

                JOptionPane.showMessageDialog(null, "Групу товарів успішно відредаговано!");
                success = true;
            }

            groupsTextArea.setText(selectedGroup.display());
            itemsGroupComboBox.removeAllItems();
            fillComboBoxWithGroups(itemsGroupComboBox, groups);
            importComboBox.removeAllItems();
            fillComboBoxWithGroups(importComboBox, groups);
        });

        JButton groupsDeleteButton = new JButton("Видалити");
        groupsDeleteButton.setBackground(new Color(191, 214, 246));
        groupsDeleteButton.setForeground(new Color(33, 33, 33));
        groupsDeleteButton.setOpaque(true);
        groupsDeleteButton.setBorderPainted(false);
        groupsDeleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        /** Подія від натискання кнопки "Видалити" */
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

        JPanel itemsGroupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel selectItemsGroupLabel = new JLabel("Виберіть групу товарів : ");

        // Комбобокс з групами
        itemsGroupComboBox = new JComboBox<>();
        fillComboBoxWithGroups(itemsGroupComboBox, groups);

        itemsGroupPanel.add(selectItemsGroupLabel);
        itemsGroupPanel.add(itemsGroupComboBox);
        itemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        itemsPanel.add(itemsGroupPanel);

        JPanel itemsProductPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Відображення товарів (JList)
        DefaultListModel<Product> productsModel = new DefaultListModel<>();
        JList<Product> productsList = new JList<>(productsModel);
        productsList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        productsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(productsList);
        scrollPane.setPreferredSize(new Dimension(275, 300));
        itemsProductPanel.add(scrollPane);

        /** Подія від вибору елемента в комбобоксі груп */
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

        /** Подія на вибір елемента в JList товарів */
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
        itemsAddButton.setBackground(new Color(191, 214, 246));
        itemsAddButton.setForeground(new Color(33, 33, 33));
        itemsAddButton.setOpaque(true);
        itemsAddButton.setBorderPainted(false);
        itemsAddButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        /** Подія від натискання кнопки "Додати" */
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
        itemsEditButton.setBackground(new Color(191, 214, 246));
        itemsEditButton.setForeground(new Color(33, 33, 33));
        itemsEditButton.setOpaque(true);
        itemsEditButton.setBorderPainted(false);
        itemsEditButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        /** Подія від натискання кнопки "Редагувати" */
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

                    String groupName = (String) itemsGroupComboBox.getSelectedItem();
                    ProductGroup selectedGroup = findGroupByName(groupName, groups);
                    selectedProduct.edit(selectedGroup, newName, newDescription, newManufacturer, newQuantity, newPrice);

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
        itemsDeleteButton.setBackground(new Color(191, 214, 246));
        itemsDeleteButton.setForeground(new Color(33, 33, 33));
        itemsDeleteButton.setOpaque(true);
        itemsDeleteButton.setBorderPainted(false);
        itemsDeleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        /** Подія від натискання кнопки "Видалити" */
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
        selectImportLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        importComboBox = new JComboBox<>();
        fillComboBoxWithGroups(importComboBox, groups);

        choice1Panel.add(selectImportLabel);
        choice1Panel.add(importComboBox);
        importPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        importPanel.add(choice1Panel);

        importProductComboBox = new JComboBox<>();

        JPanel choice2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        choice2Panel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        choice2Panel.add(new JLabel("Виберіть товар: "));
        choice2Panel.add(importProductComboBox);

        /** Подія від вибору елементу в комбобоксі груп */
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

        // Панель для виводу інформації про обраний товар
        JPanel informationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextArea informationArea = new JTextArea(28, 35);
        informationArea.setEditable(false);
        informationArea.setLineWrap(true);
        informationArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        informationPanel.add(informationArea);
        importPanel.add(informationPanel);

        /** Подія від вибору елементу в комбобоксі товарів */
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
        writeInButton.setBackground(new Color(191, 214, 246));
        writeInButton.setForeground(new Color(33, 33, 33));
        writeInButton.setOpaque(true);
        writeInButton.setBorderPainted(false);
        writeInButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        /** Подія від натискання кнопки "Додати" */
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
                        JOptionPane.showMessageDialog(null, "Товар успішно надійшов на склад!");
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
        writeOffButton.setBackground(new Color(191, 214, 246));
        writeOffButton.setForeground(new Color(33, 33, 33));
        writeOffButton.setOpaque(true);
        writeOffButton.setBorderPainted(false);
        writeOffButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        /** Подія від натискання кнопки "Списати" */
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
        statisticPanel.setBackground(new Color(183, 226, 252));
        statisticPanel.removeAll();
        statisticPanel.setLayout(new BorderLayout());

        JLabel statisticHeader = new JLabel("Статистика");
        statisticHeader.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel headerPanel = new JPanel();
        headerPanel.add(statisticHeader);
        statisticPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        JButton allProductsBtn = new JButton("Вся продукція");
        allProductsBtn.setBackground(new Color(191, 214, 246));
        allProductsBtn.setForeground(new Color(33, 33, 33));
        allProductsBtn.setOpaque(true);
        allProductsBtn.setBorderPainted(false);
        allProductsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JButton byGroupsBtn = new JButton("По групах");
        byGroupsBtn.setBackground(new Color(191, 214, 246));
        byGroupsBtn.setForeground(new Color(33, 33, 33));
        byGroupsBtn.setOpaque(true);
        byGroupsBtn.setBorderPainted(false);
        byGroupsBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JButton totalValueBtn = new JButton("Загальна вартість складу");
        totalValueBtn.setBackground(new Color(191, 214, 246));
        totalValueBtn.setForeground(new Color(33, 33, 33));
        totalValueBtn.setOpaque(true);
        totalValueBtn.setBorderPainted(false);
        totalValueBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabsPanel.add(allProductsBtn);
        tabsPanel.add(byGroupsBtn);
        tabsPanel.add(totalValueBtn);
        statisticPanel.add(tabsPanel, BorderLayout.CENTER);

        JPanel tabContentPanel = new JPanel(new CardLayout());
        String[] columns = {"Назва", "Група", "Кількість", "Ціна", "Загальна вартість"};

        // Таблиця з усіма товарами
        JTable allProductsTable = new JTable(new Object[0][columns.length], columns);
        allProductsTable.setBackground(new Color(232, 243, 255));
        JScrollPane scrollAll = new JScrollPane(allProductsTable);
        JPanel allProductsPanel = new JPanel(new BorderLayout());

        allProductsPanel.setBorder(BorderFactory.createTitledBorder("Вся продукція"));
        allProductsPanel.add(scrollAll, BorderLayout.CENTER);

        fillAllProductsTable(allProductsTable, groups);

        JComboBox<String> group2ComboBox = new JComboBox<>();
        fillComboBoxWithGroups(group2ComboBox, groups);

        JTable groupTable = new JTable(new Object[0][columns.length], columns);
        groupTable.setBackground(new Color(232, 243, 255));
        JScrollPane scrollGroup = new JScrollPane(groupTable);
        JLabel groupTotalLabel = new JLabel("Загальна вартість групи: 0");

        /** Подія від вибору елемента у комбобоксі груп */
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

        /** Загальна вартість складу */
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

    /** Метод для заповнення комбобоксу групами */
    public void fillComboBoxWithGroups(JComboBox<String> comboBox, ArrayList<ProductGroup> groups) {
        for (ProductGroup group : groups) {
            comboBox.addItem(group.name);
        }
    }

    /** Метод для заповнення комбобоксу товарами */
    public void fillComboBoxWithProducts(JComboBox<String> comboBox, ArrayList<Product> products) {
        for (Product product : products) {
            comboBox.addItem(product.name);
        }
    }

    /** Метод для знаходження групи товарів за ім'ям */
    public ProductGroup findGroupByName(String name, ArrayList<ProductGroup> groups) {
        for (ProductGroup group : groups) {
            if (group.name.equalsIgnoreCase(name)) {
                return group;
            }
        }
        return null;
    }

    /** Метод для знаходження товару за ім'ям */
    public Product findProductByName(String name, ArrayList<Product> products) {
        for (Product product : products) {
            if (product.name.equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    /** Метод для заповнення таблиці з усіма товарами вибраної групи */
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

    /** Метод для заповнення таблиці з усіма товарами */
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

    /** Перевірка на унікальність імені */
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

    /** Метод переведення стрічки у товар */
    public Product StringToProduct(String fileString) {
        String[] parts = fileString.split(";");

        String name = parts[0];
        String description = parts[1];
        String manufacturer = parts[2];
        int stockQuantity = Integer.parseInt(parts[3]);
        double price = Double.parseDouble(parts[4]);

        return new Product(name, description, manufacturer, stockQuantity, price);
    }

    /** Метод переведення стрічки у групу товарів */
    public ProductGroup StringToProductGroup(String fileString) {
        String[] parts = fileString.split(";");

        String name = parts[0];
        String description = parts[1];

        return new ProductGroup(name, description, new ArrayList<Product>());
    }

    /** Метод для завантаження груп з файлу */
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

    /** Метод для завантаження товарів з файлу */
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

    /** Запуск */
    public static void main(String[] args) {
        NokoGUI noko = new NokoGUI();
        noko.setVisible(true);
    }
}
