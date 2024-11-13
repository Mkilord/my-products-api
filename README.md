# Product API Documentation

## Base URL
`/api/products`

## Endpoints

### 1. **Get all products**
- **Method**: `GET`
- **URL**: `/api/products`
- **Response**: Список всех продуктов.

### 2. **Get product by ID**
- **Method**: `GET`
- **URL**: `/api/products/{id}`
- **Response**: Продукт с указанным ID.

### 3. **Create a new product**
- **Method**: `POST`
- **URL**: `/api/products`
- **Request body**: JSON объект с данными нового продукта.
- **Response**: Созданный продукт с ID.

### 4. **Update an existing product**
- **Method**: `PUT`
- **URL**: `/api/products/{id}`
- **Request body**: JSON объект с обновленными данными продукта.
- **Response**: Обновленный продукт.

### 5. **Delete a product**
- **Method**: `DELETE`
- **URL**: `/api/products/{id}`
- **Response**: Сообщение об успешной операции.
