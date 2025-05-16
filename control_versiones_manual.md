
# 🗂️ Control de Versiones Manual de Base de Datos (Oracle SQL)

Este documento describe paso a paso cómo implementar un control de versiones **manual** para los scripts de tu base de datos, utilizando Oracle SQL Developer y Git.  

---

## 📌 1. Crear el Respaldo Inicial

### 🔧 Herramienta: Oracle SQL Developer

1. Abre Oracle SQL Developer y conéctate a tu base de datos.
2. Haz clic derecho sobre tu conexión → **Exportar base de datos**.
3. En el asistente de exportación:
   - Exporta **solo el esquema** (estructura), no los datos.
   - Selecciona formato: **SQL Insert Script**.
   - Elige una carpeta para guardar el archivo `.sql`, por ejemplo: `respaldo_inicial.sql`.
4. Finaliza el asistente y espera a que se genere el archivo.

---

## 🧹 2. Limpiar el Script de Respaldo

1. Abre el archivo `respaldo_inicial.sql` en tu editor de texto o código (como VS Code).
2. **Eliminar datos**:
   - Busca todas las sentencias `INSERT INTO` y elimínalas.
   - Puedes usar búsqueda por patrón:  
     ```sql
     ^INSERT INTO.*
     ```
     y eliminarlas todas.
3. **Eliminar configuración específica del entorno**:
   - Borra cualquier cláusula `TABLESPACE`, por ejemplo:
     ```sql
     TABLESPACE USERS
     ```
   - Borra cualquier cláusula `COLLATE`, si existe.
   - Asegúrate de dejar solo las definiciones estructurales (`CREATE TABLE`, `CREATE INDEX`, etc.).
4. Guarda el archivo limpio con un nuevo nombre: `base_sin_datos.sql`.

---

## 🔁 3. Probar la Restauración

1. Abre SQL Developer y crea una nueva base de datos vacía (o borra las tablas actuales si estás trabajando localmente).
2. Ejecuta el script `base_sin_datos.sql` en esta base de datos.
3. Verifica que:
   - Se crean todas las tablas, relaciones y objetos.
   - No se insertan datos (las tablas deben estar vacías).

---

## 🧱 4. Establecer la Versión Base

1. En tu proyecto Git, crea una nueva carpeta:
   ```
   mkdir sql-migrations
   ```
2. Copia el archivo limpio:
   ```
   cp base_sin_datos.sql sql-migrations/V0__base.sql
   ```
3. Realiza un commit:
   ```bash
   git add sql-migrations/V0__base.sql
   git commit -m "Agrega V0 base del esquema sin datos"
   git push
   ```
4. Si estás trabajando en una rama diferente, haz merge a la rama principal.

---

## 🆕 5. Crear la Primera Migración

### Ejemplo: Agregar columna `intereses` a la tabla `usuarios`

1. En tu base de datos local, ejecuta el siguiente comando:

   ```sql
   ALTER TABLE usuarios ADD (intereses VARCHAR2(100));
   ```

2. Verifica que:
   - La columna `intereses` fue añadida correctamente.
   - No se alteró información importante de otras columnas.

3. Crea un nuevo archivo en la carpeta `sql-migrations` con el siguiente nombre:
   ```
   V1__20240513_agrega_columna_intereses.sql
   ```

4. El contenido del archivo debe ser **solo**:
   ```sql
   ALTER TABLE usuarios ADD (intereses VARCHAR2(100));
   ```

5. Realiza un nuevo commit:
   ```bash
   git add sql-migrations/V1__20240513_agrega_columna_intereses.sql
   git commit -m "Agrega columna intereses a usuarios"
   git push
   ```

---

## ✅ Buenas Prácticas

- Nombra los archivos siguiendo el formato `V<versión>__<fecha>_<descripcion>.sql`.
- Nunca incluyas datos (`INSERT`) en estos scripts.
- Asegúrate de probar cada script en tu entorno local antes de hacer commit.
- Documenta cada cambio estructural como una migración nueva.
