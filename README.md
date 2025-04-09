# Inventory

---

### Introduction

This is a simple Android App I've been slowly working on, to build my understanding of 
Android development. 

---

### Features

- **Add Items**: Take a picture of an item and add it to your inventory.
- **Edit Items**: Modify existing items' details.
- **Category Management**: Organize items into categories for easy filtering.
- **Camera Integration**: Use your device's camera to capture item images.
- **Image Gallery**: View all images of your inventory items.
- **Navigation Drawer**: Easily navigate between different sections of the app

---

### Project Structure

The project is structured as an Android application using the MVVM (Model-View-ViewModel) architectural pattern. Here's an overview of the main components:

#### MainActivity

- **File**: `app/src/main/java/com/hattonky/inventory/MainActivity.java`
- **Description**: The main entry point of the application. It handles the user interface, including the RecyclerView for displaying items, a Spinner for category filtering, and a Navigation Drawer for app navigation.
- **Key Features**:
  - Implements `ItemAdapter.OnItemClickListener` to handle item clicks.
  - Uses `ItemViewModel` and `CategoryViewModel` to manage data.
  - Fetches and displays categories in a Spinner.
  - Handles navigation to other activities like `AddEditItemActivity`, `CameraActivity`, `AddCategoryActivity`, and `ImageGalleryActivity`.

#### ItemViewModel and CategoryViewModel

- **Description**: These ViewModels manage the data for items and categories, respectively. They use LiveData to provide data to the UI components.
- **Interaction**: The `MainActivity` observes data changes from these ViewModels to update the UI.

#### RecyclerView and ItemAdapter

- **Description**: The RecyclerView displays a list of items, and the `ItemAdapter` manages the data and layout for each item in the list.
- **Interaction**: When an item is clicked, `MainActivity` is notified through the `onItemClick` method, which then navigates to `AddEditItemActivity`.

#### Spinner and Category Filtering

- **Description**: The Spinner allows users to filter items by category. It's populated with categories fetched from the `CategoryViewModel`.
- **Interaction**: When a category is selected, `MainActivity` uses the `ItemViewModel` to fetch and display the appropriate items.

#### Navigation Drawer

- **Description**: Implemented using `DrawerLayout` and `NavigationView`, it provides navigation options to different parts of the app.
- **Interaction**: Selecting an option in the drawer triggers navigation to the corresponding activity.

---

### How Components Interact

1. **Data Flow**:
   - `CategoryViewModel` fetches categories and notifies `MainActivity` through LiveData.
   - `MainActivity` updates the Spinner with the fetched categories.
   - When a category is selected, `MainActivity` uses `ItemViewModel` to fetch and display items.
   - `ItemViewModel` fetches items and notifies `MainActivity` to update the RecyclerView.

2. **User Interaction**:
   - Clicking an item in the RecyclerView triggers `onItemClick` in `MainActivity`, which navigates to `AddEditItemActivity`.
   - Selecting an option in the Navigation Drawer navigates to the corresponding activity.
   - Selecting a category in the Spinner filters the items displayed in the RecyclerView.

3. **Navigation**:
   - `MainActivity` uses Intents to navigate to other activities like `AddEditItemActivity`, `CameraActivity`, `AddCategoryActivity`, and `ImageGalleryActivity`.

---

### Getting Started

To get started with the Inventory App, follow these steps:

1. **Clone the Repository**:
   ```bash
   git clone git@github.com:John-Hatton/Inventory.git
   ```

2. **Open in Android Studio**:

    Open the project in Android Studio.
    
3. **Build and Run**:

    Build the project and run it on an emulator or physical device.

---