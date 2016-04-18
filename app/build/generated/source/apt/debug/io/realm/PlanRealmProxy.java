package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;
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

public class PlanRealmProxy extends Plan
    implements RealmObjectProxy, PlanRealmProxyInterface {

    static final class PlanColumnInfo extends ColumnInfo {

        public final long idIndex;
        public final long titleIndex;
        public final long categoryIndex;
        public final long goalIndex;
        public final long progressIndex;
        public final long dueTimeIndex;

        PlanColumnInfo(String path, Table table) {
            final Map<String, Long> indicesMap = new HashMap<String, Long>(6);
            this.idIndex = getValidColumnIndex(path, table, "Plan", "id");
            indicesMap.put("id", this.idIndex);

            this.titleIndex = getValidColumnIndex(path, table, "Plan", "title");
            indicesMap.put("title", this.titleIndex);

            this.categoryIndex = getValidColumnIndex(path, table, "Plan", "category");
            indicesMap.put("category", this.categoryIndex);

            this.goalIndex = getValidColumnIndex(path, table, "Plan", "goal");
            indicesMap.put("goal", this.goalIndex);

            this.progressIndex = getValidColumnIndex(path, table, "Plan", "progress");
            indicesMap.put("progress", this.progressIndex);

            this.dueTimeIndex = getValidColumnIndex(path, table, "Plan", "dueTime");
            indicesMap.put("dueTime", this.dueTimeIndex);

            setIndicesMap(indicesMap);
        }
    }

    private final PlanColumnInfo columnInfo;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("title");
        fieldNames.add("category");
        fieldNames.add("goal");
        fieldNames.add("progress");
        fieldNames.add("dueTime");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    PlanRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (PlanColumnInfo) columnInfo;
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

    public Category realmGet$category() {
        ((RealmObject) this).realm.checkIfValid();
        if (((RealmObject) this).row.isNullLink(columnInfo.categoryIndex)) {
            return null;
        }
        return ((RealmObject) this).realm.get(com.catwoman.lifetodo.models.Category.class, ((RealmObject) this).row.getLink(columnInfo.categoryIndex));
    }

    public void realmSet$category(Category value) {
        ((RealmObject) this).realm.checkIfValid();
        if (value == null) {
            ((RealmObject) this).row.nullifyLink(columnInfo.categoryIndex);
            return;
        }
        if (!value.isValid()) {
            throw new IllegalArgumentException("'value' is not a valid managed object.");
        }
        if (value.realm != this.realm) {
            throw new IllegalArgumentException("'value' belongs to a different Realm.");
        }
        ((RealmObject) this).row.setLink(columnInfo.categoryIndex, ((RealmObject) value).row.getIndex());
    }

    @SuppressWarnings("cast")
    public int realmGet$goal() {
        ((RealmObject) this).realm.checkIfValid();
        return (int) ((RealmObject) this).row.getLong(columnInfo.goalIndex);
    }

    public void realmSet$goal(int value) {
        ((RealmObject) this).realm.checkIfValid();
        ((RealmObject) this).row.setLong(columnInfo.goalIndex, value);
    }

    @SuppressWarnings("cast")
    public int realmGet$progress() {
        ((RealmObject) this).realm.checkIfValid();
        return (int) ((RealmObject) this).row.getLong(columnInfo.progressIndex);
    }

    public void realmSet$progress(int value) {
        ((RealmObject) this).realm.checkIfValid();
        ((RealmObject) this).row.setLong(columnInfo.progressIndex, value);
    }

    @SuppressWarnings("cast")
    public long realmGet$dueTime() {
        ((RealmObject) this).realm.checkIfValid();
        return (long) ((RealmObject) this).row.getLong(columnInfo.dueTimeIndex);
    }

    public void realmSet$dueTime(long value) {
        ((RealmObject) this).realm.checkIfValid();
        ((RealmObject) this).row.setLong(columnInfo.dueTimeIndex, value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_Plan")) {
            Table table = transaction.getTable("class_Plan");
            table.addColumn(RealmFieldType.INTEGER, "id", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.STRING, "title", Table.NULLABLE);
            if (!transaction.hasTable("class_Category")) {
                CategoryRealmProxy.initTable(transaction);
            }
            table.addColumnLink(RealmFieldType.OBJECT, "category", transaction.getTable("class_Category"));
            table.addColumn(RealmFieldType.INTEGER, "goal", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "progress", Table.NOT_NULLABLE);
            table.addColumn(RealmFieldType.INTEGER, "dueTime", Table.NOT_NULLABLE);
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_Plan");
    }

    public static PlanColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_Plan")) {
            Table table = transaction.getTable("class_Plan");
            if (table.getColumnCount() != 6) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 6 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<String, RealmFieldType>();
            for (long i = 0; i < 6; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            final PlanColumnInfo columnInfo = new PlanColumnInfo(transaction.getPath(), table);

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
            if (!columnTypes.containsKey("category")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'category' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("category") != RealmFieldType.OBJECT) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'Category' for field 'category'");
            }
            if (!transaction.hasTable("class_Category")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing class 'class_Category' for field 'category'");
            }
            Table table_2 = transaction.getTable("class_Category");
            if (!table.getLinkTarget(columnInfo.categoryIndex).hasSameSchema(table_2)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid RealmObject for field 'category': '" + table.getLinkTarget(columnInfo.categoryIndex).getName() + "' expected - was '" + table_2.getName() + "'");
            }
            if (!columnTypes.containsKey("goal")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'goal' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("goal") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'goal' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.goalIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'goal' does support null values in the existing Realm file. Use corresponding boxed type for field 'goal' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            if (!columnTypes.containsKey("progress")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'progress' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("progress") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'progress' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.progressIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'progress' does support null values in the existing Realm file. Use corresponding boxed type for field 'progress' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            if (!columnTypes.containsKey("dueTime")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'dueTime' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("dueTime") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'long' for field 'dueTime' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.dueTimeIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'dueTime' does support null values in the existing Realm file. Use corresponding boxed type for field 'dueTime' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            return columnInfo;
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The Plan class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_Plan";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    @SuppressWarnings("cast")
    public static Plan createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        Plan obj = null;
        if (update) {
            Table table = realm.getTable(Plan.class);
            long pkColumnIndex = table.getPrimaryKey();
            if (!json.isNull("id")) {
                long rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
                if (rowIndex != TableOrView.NO_MATCH) {
                    obj = new PlanRealmProxy(realm.schema.getColumnInfo(Plan.class));
                    ((RealmObject) obj).realm = realm;
                    ((RealmObject) obj).row = table.getUncheckedRow(rowIndex);
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (PlanRealmProxy) realm.createObject(Plan.class, null);
                } else {
                    obj = (PlanRealmProxy) realm.createObject(Plan.class, json.getInt("id"));
                }
            } else {
                obj = (PlanRealmProxy) realm.createObject(Plan.class);
            }
        }
        if (json.has("id")) {
            if (json.isNull("id")) {
                throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
            } else {
                ((PlanRealmProxyInterface) obj).realmSet$id((int) json.getInt("id"));
            }
        }
        if (json.has("title")) {
            if (json.isNull("title")) {
                ((PlanRealmProxyInterface) obj).realmSet$title(null);
            } else {
                ((PlanRealmProxyInterface) obj).realmSet$title((String) json.getString("title"));
            }
        }
        if (json.has("category")) {
            if (json.isNull("category")) {
                ((PlanRealmProxyInterface) obj).realmSet$category(null);
            } else {
                com.catwoman.lifetodo.models.Category categoryObj = CategoryRealmProxy.createOrUpdateUsingJsonObject(realm, json.getJSONObject("category"), update);
                ((PlanRealmProxyInterface) obj).realmSet$category(categoryObj);
            }
        }
        if (json.has("goal")) {
            if (json.isNull("goal")) {
                throw new IllegalArgumentException("Trying to set non-nullable field goal to null.");
            } else {
                ((PlanRealmProxyInterface) obj).realmSet$goal((int) json.getInt("goal"));
            }
        }
        if (json.has("progress")) {
            if (json.isNull("progress")) {
                throw new IllegalArgumentException("Trying to set non-nullable field progress to null.");
            } else {
                ((PlanRealmProxyInterface) obj).realmSet$progress((int) json.getInt("progress"));
            }
        }
        if (json.has("dueTime")) {
            if (json.isNull("dueTime")) {
                throw new IllegalArgumentException("Trying to set non-nullable field dueTime to null.");
            } else {
                ((PlanRealmProxyInterface) obj).realmSet$dueTime((long) json.getLong("dueTime"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    public static Plan createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        Plan obj = realm.createObject(Plan.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field id to null.");
                } else {
                    ((PlanRealmProxyInterface) obj).realmSet$id((int) reader.nextInt());
                }
            } else if (name.equals("title")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((PlanRealmProxyInterface) obj).realmSet$title(null);
                } else {
                    ((PlanRealmProxyInterface) obj).realmSet$title((String) reader.nextString());
                }
            } else if (name.equals("category")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    ((PlanRealmProxyInterface) obj).realmSet$category(null);
                } else {
                    com.catwoman.lifetodo.models.Category categoryObj = CategoryRealmProxy.createUsingJsonStream(realm, reader);
                    ((PlanRealmProxyInterface) obj).realmSet$category(categoryObj);
                }
            } else if (name.equals("goal")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field goal to null.");
                } else {
                    ((PlanRealmProxyInterface) obj).realmSet$goal((int) reader.nextInt());
                }
            } else if (name.equals("progress")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field progress to null.");
                } else {
                    ((PlanRealmProxyInterface) obj).realmSet$progress((int) reader.nextInt());
                }
            } else if (name.equals("dueTime")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field dueTime to null.");
                } else {
                    ((PlanRealmProxyInterface) obj).realmSet$dueTime((long) reader.nextLong());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static Plan copyOrUpdate(Realm realm, Plan object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (((RealmObject) object).realm != null && ((RealmObject) object).realm.threadId != realm.threadId) {
            throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
        }
        if (((RealmObject) object).realm != null && ((RealmObject) object).realm.getPath().equals(realm.getPath())) {
            return object;
        }
        Plan realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(Plan.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = table.findFirstLong(pkColumnIndex, ((PlanRealmProxyInterface) object).realmGet$id());
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new PlanRealmProxy(realm.schema.getColumnInfo(Plan.class));
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

    public static Plan copy(Realm realm, Plan newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        Plan realmObject = realm.createObject(Plan.class, ((PlanRealmProxyInterface) newObject).realmGet$id());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        ((PlanRealmProxyInterface) realmObject).realmSet$id(((PlanRealmProxyInterface) newObject).realmGet$id());
        ((PlanRealmProxyInterface) realmObject).realmSet$title(((PlanRealmProxyInterface) newObject).realmGet$title());

        com.catwoman.lifetodo.models.Category categoryObj = ((PlanRealmProxyInterface) newObject).realmGet$category();
        if (categoryObj != null) {
            com.catwoman.lifetodo.models.Category cachecategory = (com.catwoman.lifetodo.models.Category) cache.get(categoryObj);
            if (cachecategory != null) {
                ((PlanRealmProxyInterface) realmObject).realmSet$category(cachecategory);
            } else {
                ((PlanRealmProxyInterface) realmObject).realmSet$category(CategoryRealmProxy.copyOrUpdate(realm, categoryObj, update, cache));
            }
        } else {
            ((PlanRealmProxyInterface) realmObject).realmSet$category(null);
        }
        ((PlanRealmProxyInterface) realmObject).realmSet$goal(((PlanRealmProxyInterface) newObject).realmGet$goal());
        ((PlanRealmProxyInterface) realmObject).realmSet$progress(((PlanRealmProxyInterface) newObject).realmGet$progress());
        ((PlanRealmProxyInterface) realmObject).realmSet$dueTime(((PlanRealmProxyInterface) newObject).realmGet$dueTime());
        return realmObject;
    }

    public static Plan createDetachedCopy(Plan realmObject, int currentDepth, int maxDepth, Map<RealmObject, CacheData<RealmObject>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmObject> cachedObject = cache.get(realmObject);
        Plan standaloneObject;
        if (cachedObject != null) {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (Plan)cachedObject.object;
            } else {
                standaloneObject = (Plan)cachedObject.object;
                cachedObject.minDepth = currentDepth;
            }
        } else {
            standaloneObject = new Plan();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmObject>(currentDepth, standaloneObject));
        }
        ((PlanRealmProxyInterface) standaloneObject).realmSet$id(((PlanRealmProxyInterface) realmObject).realmGet$id());
        ((PlanRealmProxyInterface) standaloneObject).realmSet$title(((PlanRealmProxyInterface) realmObject).realmGet$title());

        // Deep copy of category
        ((PlanRealmProxyInterface) standaloneObject).realmSet$category(CategoryRealmProxy.createDetachedCopy(((PlanRealmProxyInterface) realmObject).realmGet$category(), currentDepth + 1, maxDepth, cache));
        ((PlanRealmProxyInterface) standaloneObject).realmSet$goal(((PlanRealmProxyInterface) realmObject).realmGet$goal());
        ((PlanRealmProxyInterface) standaloneObject).realmSet$progress(((PlanRealmProxyInterface) realmObject).realmGet$progress());
        ((PlanRealmProxyInterface) standaloneObject).realmSet$dueTime(((PlanRealmProxyInterface) realmObject).realmGet$dueTime());
        return standaloneObject;
    }

    static Plan update(Realm realm, Plan realmObject, Plan newObject, Map<RealmObject, RealmObjectProxy> cache) {
        ((PlanRealmProxyInterface) realmObject).realmSet$title(((PlanRealmProxyInterface) newObject).realmGet$title());
        Category categoryObj = ((PlanRealmProxyInterface) newObject).realmGet$category();
        if (categoryObj != null) {
            Category cachecategory = (Category) cache.get(categoryObj);
            if (cachecategory != null) {
                ((PlanRealmProxyInterface) realmObject).realmSet$category(cachecategory);
            } else {
                ((PlanRealmProxyInterface) realmObject).realmSet$category(CategoryRealmProxy.copyOrUpdate(realm, categoryObj, true, cache));
            }
        } else {
            ((PlanRealmProxyInterface) realmObject).realmSet$category(null);
        }
        ((PlanRealmProxyInterface) realmObject).realmSet$goal(((PlanRealmProxyInterface) newObject).realmGet$goal());
        ((PlanRealmProxyInterface) realmObject).realmSet$progress(((PlanRealmProxyInterface) newObject).realmGet$progress());
        ((PlanRealmProxyInterface) realmObject).realmSet$dueTime(((PlanRealmProxyInterface) newObject).realmGet$dueTime());
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Plan = [");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{title:");
        stringBuilder.append(realmGet$title() != null ? realmGet$title() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{category:");
        stringBuilder.append(realmGet$category() != null ? "Category" : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{goal:");
        stringBuilder.append(realmGet$goal());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{progress:");
        stringBuilder.append(realmGet$progress());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{dueTime:");
        stringBuilder.append(realmGet$dueTime());
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
        PlanRealmProxy aPlan = (PlanRealmProxy)o;

        String path = ((RealmObject) this).realm.getPath();
        String otherPath = ((RealmObject) aPlan).realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = ((RealmObject) this).row.getTable().getName();
        String otherTableName = ((RealmObject) aPlan).row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (((RealmObject) this).row.getIndex() != ((RealmObject) aPlan).row.getIndex()) return false;

        return true;
    }

}
