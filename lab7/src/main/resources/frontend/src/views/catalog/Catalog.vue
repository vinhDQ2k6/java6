<template>
  <div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Catalog Management</h1>
      <button @click="logout" class="btn btn-danger">Logout</button>
    </div>

    <div class="row">
      <!-- Categories Column -->
      <div class="col-md-3">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <h4>Categories</h4>
          <button class="btn btn-sm btn-success" @click="openCategoryModal()">
            +
          </button>
        </div>
        <div class="list-group">
          <button
            class="list-group-item list-group-item-action"
            :class="{ active: !selectedCategory }"
            @click="selectedCategory = null"
          >
            All Categories
          </button>
          <div
            v-for="cat in categories"
            :key="cat.id"
            class="list-group-item list-group-item-action d-flex justify-content-between align-items-center"
            :class="{ active: selectedCategory === cat.id }"
            @click="selectedCategory = cat.id"
          >
            <span>{{ cat.name }}</span>
            <div>
              <button
                class="btn btn-sm btn-warning me-1"
                @click.stop="openCategoryModal(cat)"
              >
                ✎
              </button>
              <button
                class="btn btn-sm btn-danger"
                @click.stop="deleteCategory(cat.id)"
              >
                ×
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Products Column -->
      <div class="col-md-9">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h4>Products</h4>
          <button class="btn btn-primary" @click="openProductModal()">
            Add Product
          </button>
        </div>
        <div class="row">
          <div
            class="col-md-4 mb-3"
            v-for="product in filteredProducts"
            :key="product.id"
          >
            <div class="card h-100">
              <div class="card-body">
                <h5 class="card-title">{{ product.name }}</h5>
                <p class="card-text text-primary fw-bold">
                  ${{ product.price }}
                </p>
                <p class="card-text text-muted small">
                  Category: {{ getCategoryName(product.categoryId) }}
                </p>
                <div class="d-flex justify-content-end">
                  <button
                    class="btn btn-sm btn-warning me-2"
                    @click="openProductModal(product)"
                  >
                    Edit
                  </button>
                  <button
                    class="btn btn-sm btn-danger"
                    @click="deleteProduct(product.id)"
                  >
                    Delete
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Category Modal (Simple Overlay) -->
    <div
      v-if="showCategoryModal"
      class="modal d-block"
      style="background: rgba(0, 0, 0, 0.5)"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              {{ editingCategory ? "Edit" : "Add" }} Category
            </h5>
            <button
              type="button"
              class="btn-close"
              @click="showCategoryModal = false"
            ></button>
          </div>
          <div class="modal-body">
            <input
              v-model="categoryForm.name"
              class="form-control"
              placeholder="Category Name"
            />
          </div>
          <div class="modal-footer">
            <button
              class="btn btn-secondary"
              @click="showCategoryModal = false"
            >
              Close
            </button>
            <button class="btn btn-primary" @click="saveCategory">Save</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Product Modal -->
    <div
      v-if="showProductModal"
      class="modal d-block"
      style="background: rgba(0, 0, 0, 0.5)"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">
              {{ editingProduct ? "Edit" : "Add" }} Product
            </h5>
            <button
              type="button"
              class="btn-close"
              @click="showProductModal = false"
            ></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label class="form-label">Name</label>
              <input v-model="productForm.name" class="form-control" />
            </div>
            <div class="mb-3">
              <label class="form-label">Price</label>
              <input
                v-model="productForm.price"
                type="number"
                class="form-control"
              />
            </div>
            <div class="mb-3">
              <label class="form-label">Category</label>
              <select v-model="productForm.categoryId" class="form-select">
                <option v-for="cat in categories" :key="cat.id" :value="cat.id">
                  {{ cat.name }}
                </option>
              </select>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showProductModal = false">
              Close
            </button>
            <button class="btn btn-primary" @click="saveProduct">Save</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import api from "@/api/axios";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";

const categories = ref([]);
const products = ref([]);
const selectedCategory = ref(null);
const authStore = useAuthStore();
const router = useRouter();

// Modal States
const showCategoryModal = ref(false);
const showProductModal = ref(false);
const editingCategory = ref(null);
const editingProduct = ref(null);

// Forms
const categoryForm = ref({ name: "" });
const productForm = ref({ name: "", price: 0, categoryId: null });

const fetchData = async () => {
  try {
    const [catRes, prodRes] = await Promise.all([
      api.get("/catalog/categories"),
      api.get("/catalog/products"),
    ]);
    categories.value = catRes.data;
    products.value = prodRes.data;
  } catch (e) {
    console.error(e);
  }
};

onMounted(fetchData);

const filteredProducts = computed(() => {
  if (!selectedCategory.value) return products.value;
  return products.value.filter((p) => p.categoryId === selectedCategory.value);
});

const getCategoryName = (id) => {
  const cat = categories.value.find((c) => c.id === id);
  return cat ? cat.name : "Unknown";
};

// Category Actions
const openCategoryModal = (category = null) => {
  editingCategory.value = category;
  categoryForm.value = category ? { ...category } : { name: "" };
  showCategoryModal.value = true;
};

const saveCategory = async () => {
  try {
    if (editingCategory.value) {
      await api.put(
        `/catalog/categories/${editingCategory.value.id}`,
        categoryForm.value
      );
    } else {
      await api.post("/catalog/categories", categoryForm.value);
    }
    showCategoryModal.value = false;
    fetchData();
  } catch (e) {
    alert("Error saving category");
  }
};

const deleteCategory = async (id) => {
  if (!confirm("Delete this category?")) return;
  try {
    await api.delete(`/catalog/categories/${id}`);
    fetchData();
  } catch (e) {
    alert("Error deleting category");
  }
};

// Product Actions
const openProductModal = (product = null) => {
  editingProduct.value = product;
  productForm.value = product
    ? { ...product }
    : { name: "", price: 0, categoryId: categories.value[0]?.id };
  showProductModal.value = true;
};

const saveProduct = async () => {
  try {
    if (editingProduct.value) {
      await api.put(
        `/catalog/products/${editingProduct.value.id}`,
        productForm.value
      );
    } else {
      await api.post("/catalog/products", productForm.value);
    }
    showProductModal.value = false;
    fetchData();
  } catch (e) {
    alert("Error saving product");
  }
};

const deleteProduct = async (id) => {
  if (!confirm("Delete this product?")) return;
  try {
    await api.delete(`/catalog/products/${id}`);
    fetchData();
  } catch (e) {
    alert("Error deleting product");
  }
};

const logout = () => {
  authStore.logout();
  router.push("/login");
};
</script>
