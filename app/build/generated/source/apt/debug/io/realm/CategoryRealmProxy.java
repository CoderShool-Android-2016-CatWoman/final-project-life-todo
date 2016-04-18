package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.catwoman.lifetodo.models.Category;
import io.realm.RealmFieldType;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryRealmProxy extends Category
    implements RealmObjectProxy, CategoryRealmProxyInterface {

    static final class CategoryColumnInfo extends ColumnInfo {

        public final long idIndex;
        public final long titleIndex;
        public final long drawableIndex;
        public final long colorIndex;

        CategoryColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(4);
            this.idIndex = getValidColumnIndex(path, table, "Category", "id");
            indicesMap.put("id", this.idIndex);

            this.titleIndex = getValidColumnIndex(path, table, "Category", "title");
            indicesMap.put("title", this.titleIndex);

            this.drawableIndex = getValidColumnIndex(path, table, "Category", "drawable");
            indicesMap.put("drawable", this.drawableIndex);

            this.colorIndex = getValidColumnIndex(path, table, "Category", "color");
            indicesMap.put("color", this.colorIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final CategoryColumnInfo columnInfo;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("title");
        fieldNames.add("drawable");
        fieldNames.add("color");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    CategoryRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (CategoryColumnInfo) columnInfo;
    }

    @SuppressWarnings("cast")
    public int realmGet$id() {
        ((RealmObject) this).realm.checkIfValid();
        return (int) ((RealmObject) this).row.getLong(columnInfo.idIndex);
    }

    public void realmSet$id(int value) {
        ((RealmObject) this).realm.checkIfValid();
        ((RealmObject) this).row.setLong(columnInfo.idIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$title() {
        ((RealmObject) this).realm.checkIfValid();
        return (java.lang.String) ((RealmObject) this).row.getString(columnInfo.titleIndex);
    }

    public void realmSet$title(String value) {
        ((RealmObject) this).realm.checkIfValid();
        if (value == null) {
            ((RealmObject) this).row.setNull(columnInfo.titleIndex);
            return;
        }
        ((RealmObject) this).row.setString(columnInfo.titleIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$drawable() {
        ((RealmObject) this).realm.checkIfValid();
        return (java.lang.String) ((RealmObject) this).row.getString(columnInfo.drawableIndex);
    }

    public void realmSet$drawable(String value) {
        ((RealmObject) this).realm.checkIfValid();
        if (value == null) {
            ((RealmObject) this).row.setNull(columnInfo.drawableIndex);
            return;
        }
        ((RealmObject) this).row.setString(columnInfo.drawableIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$color() {
        ((RealmObject) this).realm.checkIfValid();
        return (java.lang.String) ((RealmObject) this).row.getString(columnInfo.colorIndex);
    }

    public void realmSet$color(String value) {
        ((RealmObject) this).realm.checkIfValid();
        if (value == null) {
            ((RealmObject) this).row.setNull(columnInfo.colorIndex);
            return;
        }
        ((RealmObject) this).row.setString(columnInfo.colorIndex, value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_Category")) {
            Table table = transaction.getTable("class_Category");
            table.addColumn(RealmFieldType.INTEGER, "id", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.STRING, "title", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "drawable", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "color", Table.NULLABLE);
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_Category");
    }

    public static CategoryColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_Category")) {
            Table table = transaction.getTable("class_Category");
            if (table.getColumnCount() != 4) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 4 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 4; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final CategoryColumnInfo columnInfo = new CategoryColumnInfo(transaction.getPath(), table);

            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'id' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("id") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'id' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.idIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'id' does support null values in the existing Realm file. Use corresponding boxed type for field 'id' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            if (table.getPrimaryKey() != table.getColumnIndex("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Primary key not defined for field 'id' in existing Realm file. Add @PrimaryKey.");
            }
            if (!table.hasSearchIndex(table.getColumnIndex("id"))) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Index not defined for field 'id' in existing Realm file. Either set @Index or migrate using io.realm.internal.Table.removeSearchIndex().");
            }
            if (!columnTypes.containsKey("title")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'title' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("title") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'title' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.titleIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'title' is required. Either set @Required to field 'title' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("drawable")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'drawable' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("drawable") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'drawable' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.drawableIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'drawable' is required. Either set @Required to field 'drawable' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("color")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'color' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("color") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'color' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.colorIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'color' is required. Either set @Required to field 'color' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The Category class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_Category";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static Category createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        Category obj = null;
        if (update) {
            Table table = realm.getTable(Category.class);
            long pkColumnIndex = table.getPrimaryKey();
            if (!json.isNull("id")) {
                long rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
                if (rowIndex != TableOrView.NO_MATCH) {
                    obj = new CategoryRealmProxy(realm.schema.getColumnInfo(Category.class));
                    ((RealmObject) obj).realm = realm;
                    ((RealmObject) obj).row = table.getUncheckedRow(rowIndex);
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (CategoryRealmProxy) realm.createObject(Category.class, null);
                } else {
                    obj = (CategoryRealmProxy) realm.createObject(Category.class, json.getInt("id"));
                }
            } else {
                obj = (CategoryRealmProxy) realm.createObject(Category.class);
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
            } else {
                ((CategoryRealmProxyInterface) obj).realmSet$id((int) json.getInt("id"));
            }
        }
        if (json.has("title")) {
            if (json.isNull("title")) {
                ((CategoryRealmProxyInterface) obj).realmSet$title(null);
            } else {
                ((CategoryRealmProxyInterface) obj).realmSet$title((String) json.getString("title"));
            }
        }
        if (json.has("drawable")) {
            if (json.isNull("drawable")) {
                ((CategoryRealmProxyInterface) obj).realmSet$drawable(null);
            } else {
                ((CategoryRealmProxyInterface) obj).realmSet$drawable((String) json.getString("drawable"));
            }
        }
        if (json.has("color")) {
            if (json.isNull("color")) {
                ((CategoryRealmProxyInterface) obj).realmSet$color(null);
            } else {
                ((CategoryRealmProxyInterface) obj).realmSet$color((String) json.getString("color"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static Category createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        Category obj = realm.createObject(Category.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
                } else {
                    ((CategoryRealmProxyInterface) obj).realmSet$id((int) reader.nextInt());
                }
            } else if (name.equals("title")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CategoryRealmProxyInterface) obj).realmSet$title(null);
                } else {
                    ((CategoryRealmProxyInterface) obj).realmSet$title((String) reader.nextString());
                }
            } else if (name.equals("drawable")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CategoryRealmProxyInterface) obj).realmSet$drawable(null);
                } else {
                    ((CategoryRealmProxyInterface) obj).realmSet$drawable((String) reader.nextString());
                }
            } else if (name.equals("color")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((CategoryRealmProxyInterface) obj).realmSet$color(null);
                } else {
                    ((CategoryRealmProxyInterface) obj).realmSet$color((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static Category copyOrUpdate(Realm realm, Category object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (((RealmObject) object).realm != null && ((RealmObject) object).realm.threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (((RealmObject) object).realm != null && ((RealmObject) object).realm.getPath().equals(realm.getPath())) {
            return object;
        }
        Category realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(Category.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = table.findFirstLong(pkColumnIndex, ((CategoryRealmProxyInterface) object).realmGet$id());
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new CategoryRealmProxy(realm.schema.getColumnInfo(Category.class));
                ((RealmObject) realmObject).realm = realm;
                ((RealmObject) realmObject).row = table.getUncheckedRow(rowIndex);
                cache.put(object, (RealmObjectProxy) realmObject);
            } else {
                canUpdate = false;
            }
        }

        if (canUpdate) {
            return update(realm, realmObject, object, cache);
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static Category copy(Realm realm, Category newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        Category realmObject = realm.createObject(Category.class, ((CategoryRealmProxyInterface) newObject).realmGet$id());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        ((CategoryRealmProxyInterface) realmObject).realmSet$id(((CategoryRealmProxyInterface) newObject).realmGet$id());
        ((CategoryRealmProxyInterface) realmObject).realmSet$title(((CategoryRealmProxyInterface) newObject).realmGet$title());
        ((CategoryRealmProxyInterface) realmObject).realmSet$drawable(((CategoryRealmProxyInterface) newObject).realmGet$drawable());
        ((CategoryRealmProxyInterface) realmObject).realmSet$color(((CategoryRealmProxyInterface) newObject).realmGet$color());
        return realmObject;
    }

    public static Category createDetachedCopy(Category realmObject, int currentDepth, int maxDepth, Map<RealmObject, CacheData<RealmObject>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmObject> cachedObject = cache.get(realmObject);
        Category standaloneObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (Category)cachedObject.object;
            } else {
                standaloneObject = (Category)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            standaloneObject = new Category();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmObject>(currentDepth, standaloneObject));
        }
        ((CategoryRealmProxyInterface) standaloneObject).realmSet$id(((CategoryRealmProxyInterface) realmObject).realmGet$id());
        ((CategoryRealmProxyInterface) standaloneObject).realmSet$title(((CategoryRealmProxyInterface) realmObject).realmGet$title());
        ((CategoryRealmProxyInterface) standaloneObject).realmSet$drawable(((CategoryRealmProxyInterface) realmObject).realmGet$drawable());
        ((CategoryRealmProxyInterface) standaloneObject).realmSet$color(((CategoryRealmProxyInterface) realmObject).realmGet$color());
        return standaloneObject;
    }

    static Category update(Realm realm, Category realmObject, Category newObject, Map<RealmObject, RealmObjectProxy> cache) {
        ((CategoryRealmProxyInterface) realmObject).realmSet$title(((CategoryRealmProxyInterface) newObject).realmGet$title());
        ((CategoryRealmProxyInterface) realmObject).realmSet$drawable(((CategoryRealmProxyInterface) newObject).realmGet$drawable());
        ((CategoryRealmProxyInterface) realmObject).realmSet$color(((CategoryRealmProxyInterface) newObject).realmGet$color());
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Category = [");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{title:");
        stringBuilder.append(realmGet$title() != null ? realmGet$title() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{drawable:");
        stringBuilder.append(realmGet$drawable() != null ? realmGet$drawable() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{color:");
        stringBuilder.append(realmGet$color() != null ? realmGet$color() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        String realmName = ((RealmObject) this).realm.getPath();
        String tableName = ((RealmObject) this).row.getTable().getName();
        long rowIndex = ((RealmObject) this).row.getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryRealmProxy aCategory = (CategoryRealmProxy)o;

        String path = ((RealmObject) this).realm.getPath();
        String otherPath = ((RealmObject) aCategory).realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = ((RealmObject) this).row.getTable().getName();
        String otherTableName = ((RealmObject) aCategory).row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (((RealmObject) this).row.getIndex() != ((RealmObject) aCategory).row.getIndex()) return false;

        return true;
    }

}
