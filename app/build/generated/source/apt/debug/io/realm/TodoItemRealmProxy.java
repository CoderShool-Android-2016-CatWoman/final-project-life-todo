package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.catwoman.lifetodo.models.TodoItem;
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

public class TodoItemRealmProxy extends TodoItem
    implements RealmObjectProxy, TodoItemRealmProxyInterface {

    static final class TodoItemColumnInfo extends ColumnInfo {

        public final long idIndex;
        public final long itemNameIndex;
        public final long itemThumbUrlIndex;
        public final long itemStatusIndex;
        public final long itemDescriptionIndex;

        TodoItemColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(5);
            this.idIndex = getValidColumnIndex(path, table, "TodoItem", "id");
            indicesMap.put("id", this.idIndex);

            this.itemNameIndex = getValidColumnIndex(path, table, "TodoItem", "itemName");
            indicesMap.put("itemName", this.itemNameIndex);

            this.itemThumbUrlIndex = getValidColumnIndex(path, table, "TodoItem", "itemThumbUrl");
            indicesMap.put("itemThumbUrl", this.itemThumbUrlIndex);

            this.itemStatusIndex = getValidColumnIndex(path, table, "TodoItem", "itemStatus");
            indicesMap.put("itemStatus", this.itemStatusIndex);

            this.itemDescriptionIndex = getValidColumnIndex(path, table, "TodoItem", "itemDescription");
            indicesMap.put("itemDescription", this.itemDescriptionIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final TodoItemColumnInfo columnInfo;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("itemName");
        fieldNames.add("itemThumbUrl");
        fieldNames.add("itemStatus");
        fieldNames.add("itemDescription");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    TodoItemRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (TodoItemColumnInfo) columnInfo;
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
    public String realmGet$itemName() {
        ((RealmObject) this).realm.checkIfValid();
        return (java.lang.String) ((RealmObject) this).row.getString(columnInfo.itemNameIndex);
    }

    public void realmSet$itemName(String value) {
        ((RealmObject) this).realm.checkIfValid();
        if (value == null) {
            ((RealmObject) this).row.setNull(columnInfo.itemNameIndex);
            return;
        }
        ((RealmObject) this).row.setString(columnInfo.itemNameIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$itemThumbUrl() {
        ((RealmObject) this).realm.checkIfValid();
        return (java.lang.String) ((RealmObject) this).row.getString(columnInfo.itemThumbUrlIndex);
    }

    public void realmSet$itemThumbUrl(String value) {
        ((RealmObject) this).realm.checkIfValid();
        if (value == null) {
            ((RealmObject) this).row.setNull(columnInfo.itemThumbUrlIndex);
            return;
        }
        ((RealmObject) this).row.setString(columnInfo.itemThumbUrlIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$itemStatus() {
        ((RealmObject) this).realm.checkIfValid();
        return (java.lang.String) ((RealmObject) this).row.getString(columnInfo.itemStatusIndex);
    }

    public void realmSet$itemStatus(String value) {
        ((RealmObject) this).realm.checkIfValid();
        if (value == null) {
            ((RealmObject) this).row.setNull(columnInfo.itemStatusIndex);
            return;
        }
        ((RealmObject) this).row.setString(columnInfo.itemStatusIndex, value);
    }

    @SuppressWarnings("cast")
    public String realmGet$itemDescription() {
        ((RealmObject) this).realm.checkIfValid();
        return (java.lang.String) ((RealmObject) this).row.getString(columnInfo.itemDescriptionIndex);
    }

    public void realmSet$itemDescription(String value) {
        ((RealmObject) this).realm.checkIfValid();
        if (value == null) {
            ((RealmObject) this).row.setNull(columnInfo.itemDescriptionIndex);
            return;
        }
        ((RealmObject) this).row.setString(columnInfo.itemDescriptionIndex, value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_TodoItem")) {
            Table table = transaction.getTable("class_TodoItem");
            table.addColumn(RealmFieldType.INTEGER, "id", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.STRING, "itemName", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "itemThumbUrl", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "itemStatus", Table.NULLABLE);
            table.addColumn(RealmFieldType.STRING, "itemDescription", Table.NULLABLE);
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_TodoItem");
    }

    public static TodoItemColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_TodoItem")) {
            Table table = transaction.getTable("class_TodoItem");
            if (table.getColumnCount() != 5) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 5 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 5; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final TodoItemColumnInfo columnInfo = new TodoItemColumnInfo(transaction.getPath(), table);

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
            if (!columnTypes.containsKey("itemName")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'itemName' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("itemName") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'itemName' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.itemNameIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'itemName' is required. Either set @Required to field 'itemName' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("itemThumbUrl")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'itemThumbUrl' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("itemThumbUrl") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'itemThumbUrl' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.itemThumbUrlIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'itemThumbUrl' is required. Either set @Required to field 'itemThumbUrl' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("itemStatus")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'itemStatus' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("itemStatus") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'itemStatus' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.itemStatusIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'itemStatus' is required. Either set @Required to field 'itemStatus' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("itemDescription")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'itemDescription' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("itemDescription") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'itemDescription' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.itemDescriptionIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'itemDescription' is required. Either set @Required to field 'itemDescription' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The TodoItem class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_TodoItem";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static TodoItem createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        TodoItem obj = null;
        if (update) {
            Table table = realm.getTable(TodoItem.class);
            long pkColumnIndex = table.getPrimaryKey();
            if (!json.isNull("id")) {
                long rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
                if (rowIndex != TableOrView.NO_MATCH) {
                    obj = new TodoItemRealmProxy(realm.schema.getColumnInfo(TodoItem.class));
                    ((RealmObject) obj).realm = realm;
                    ((RealmObject) obj).row = table.getUncheckedRow(rowIndex);
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (TodoItemRealmProxy) realm.createObject(TodoItem.class, null);
                } else {
                    obj = (TodoItemRealmProxy) realm.createObject(TodoItem.class, json.getInt("id"));
                }
            } else {
                obj = (TodoItemRealmProxy) realm.createObject(TodoItem.class);
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
            } else {
                ((TodoItemRealmProxyInterface) obj).realmSet$id((int) json.getInt("id"));
            }
        }
        if (json.has("itemName")) {
            if (json.isNull("itemName")) {
                ((TodoItemRealmProxyInterface) obj).realmSet$itemName(null);
            } else {
                ((TodoItemRealmProxyInterface) obj).realmSet$itemName((String) json.getString("itemName"));
            }
        }
        if (json.has("itemThumbUrl")) {
            if (json.isNull("itemThumbUrl")) {
                ((TodoItemRealmProxyInterface) obj).realmSet$itemThumbUrl(null);
            } else {
                ((TodoItemRealmProxyInterface) obj).realmSet$itemThumbUrl((String) json.getString("itemThumbUrl"));
            }
        }
        if (json.has("itemStatus")) {
            if (json.isNull("itemStatus")) {
                ((TodoItemRealmProxyInterface) obj).realmSet$itemStatus(null);
            } else {
                ((TodoItemRealmProxyInterface) obj).realmSet$itemStatus((String) json.getString("itemStatus"));
            }
        }
        if (json.has("itemDescription")) {
            if (json.isNull("itemDescription")) {
                ((TodoItemRealmProxyInterface) obj).realmSet$itemDescription(null);
            } else {
                ((TodoItemRealmProxyInterface) obj).realmSet$itemDescription((String) json.getString("itemDescription"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static TodoItem createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        TodoItem obj = realm.createObject(TodoItem.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
                } else {
                    ((TodoItemRealmProxyInterface) obj).realmSet$id((int) reader.nextInt());
                }
            } else if (name.equals("itemName")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((TodoItemRealmProxyInterface) obj).realmSet$itemName(null);
                } else {
                    ((TodoItemRealmProxyInterface) obj).realmSet$itemName((String) reader.nextString());
                }
            } else if (name.equals("itemThumbUrl")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((TodoItemRealmProxyInterface) obj).realmSet$itemThumbUrl(null);
                } else {
                    ((TodoItemRealmProxyInterface) obj).realmSet$itemThumbUrl((String) reader.nextString());
                }
            } else if (name.equals("itemStatus")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((TodoItemRealmProxyInterface) obj).realmSet$itemStatus(null);
                } else {
                    ((TodoItemRealmProxyInterface) obj).realmSet$itemStatus((String) reader.nextString());
                }
            } else if (name.equals("itemDescription")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((TodoItemRealmProxyInterface) obj).realmSet$itemDescription(null);
                } else {
                    ((TodoItemRealmProxyInterface) obj).realmSet$itemDescription((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static TodoItem copyOrUpdate(Realm realm, TodoItem object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (((RealmObject) object).realm != null && ((RealmObject) object).realm.threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (((RealmObject) object).realm != null && ((RealmObject) object).realm.getPath().equals(realm.getPath())) {
            return object;
        }
        TodoItem realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(TodoItem.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = table.findFirstLong(pkColumnIndex, ((TodoItemRealmProxyInterface) object).realmGet$id());
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new TodoItemRealmProxy(realm.schema.getColumnInfo(TodoItem.class));
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

    public static TodoItem copy(Realm realm, TodoItem newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        TodoItem realmObject = realm.createObject(TodoItem.class, ((TodoItemRealmProxyInterface) newObject).realmGet$id());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        ((TodoItemRealmProxyInterface) realmObject).realmSet$id(((TodoItemRealmProxyInterface) newObject).realmGet$id());
        ((TodoItemRealmProxyInterface) realmObject).realmSet$itemName(((TodoItemRealmProxyInterface) newObject).realmGet$itemName());
        ((TodoItemRealmProxyInterface) realmObject).realmSet$itemThumbUrl(((TodoItemRealmProxyInterface) newObject).realmGet$itemThumbUrl());
        ((TodoItemRealmProxyInterface) realmObject).realmSet$itemStatus(((TodoItemRealmProxyInterface) newObject).realmGet$itemStatus());
        ((TodoItemRealmProxyInterface) realmObject).realmSet$itemDescription(((TodoItemRealmProxyInterface) newObject).realmGet$itemDescription());
        return realmObject;
    }

    public static TodoItem createDetachedCopy(TodoItem realmObject, int currentDepth, int maxDepth, Map<RealmObject, CacheData<RealmObject>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmObject> cachedObject = cache.get(realmObject);
        TodoItem standaloneObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (TodoItem)cachedObject.object;
            } else {
                standaloneObject = (TodoItem)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            standaloneObject = new TodoItem();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmObject>(currentDepth, standaloneObject));
        }
        ((TodoItemRealmProxyInterface) standaloneObject).realmSet$id(((TodoItemRealmProxyInterface) realmObject).realmGet$id());
        ((TodoItemRealmProxyInterface) standaloneObject).realmSet$itemName(((TodoItemRealmProxyInterface) realmObject).realmGet$itemName());
        ((TodoItemRealmProxyInterface) standaloneObject).realmSet$itemThumbUrl(((TodoItemRealmProxyInterface) realmObject).realmGet$itemThumbUrl());
        ((TodoItemRealmProxyInterface) standaloneObject).realmSet$itemStatus(((TodoItemRealmProxyInterface) realmObject).realmGet$itemStatus());
        ((TodoItemRealmProxyInterface) standaloneObject).realmSet$itemDescription(((TodoItemRealmProxyInterface) realmObject).realmGet$itemDescription());
        return standaloneObject;
    }

    static TodoItem update(Realm realm, TodoItem realmObject, TodoItem newObject, Map<RealmObject, RealmObjectProxy> cache) {
        ((TodoItemRealmProxyInterface) realmObject).realmSet$itemName(((TodoItemRealmProxyInterface) newObject).realmGet$itemName());
        ((TodoItemRealmProxyInterface) realmObject).realmSet$itemThumbUrl(((TodoItemRealmProxyInterface) newObject).realmGet$itemThumbUrl());
        ((TodoItemRealmProxyInterface) realmObject).realmSet$itemStatus(((TodoItemRealmProxyInterface) newObject).realmGet$itemStatus());
        ((TodoItemRealmProxyInterface) realmObject).realmSet$itemDescription(((TodoItemRealmProxyInterface) newObject).realmGet$itemDescription());
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("TodoItem = [");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{itemName:");
        stringBuilder.append(realmGet$itemName() != null ? realmGet$itemName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{itemThumbUrl:");
        stringBuilder.append(realmGet$itemThumbUrl() != null ? realmGet$itemThumbUrl() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{itemStatus:");
        stringBuilder.append(realmGet$itemStatus() != null ? realmGet$itemStatus() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{itemDescription:");
        stringBuilder.append(realmGet$itemDescription() != null ? realmGet$itemDescription() : "null");
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
        TodoItemRealmProxy aTodoItem = (TodoItemRealmProxy)o;

        String path = ((RealmObject) this).realm.getPath();
        String otherPath = ((RealmObject) aTodoItem).realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = ((RealmObject) this).row.getTable().getName();
        String otherTableName = ((RealmObject) aTodoItem).row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (((RealmObject) this).row.getIndex() != ((RealmObject) aTodoItem).row.getIndex()) return false;

        return true;
    }

}
