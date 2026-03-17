package com.smartpantry.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.smartpantry.data.model.MealLog;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MealLogDao_Impl implements MealLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MealLog> __insertionAdapterOfMealLog;

  private final EntityDeletionOrUpdateAdapter<MealLog> __deletionAdapterOfMealLog;

  public MealLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMealLog = new EntityInsertionAdapter<MealLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `meal_logs` (`id`,`pantryItemId`,`itemName`,`caloriesConsumed`,`quantityConsumed`,`unit`,`timestamp`,`userId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPantryItemId());
        statement.bindString(3, entity.getItemName());
        statement.bindLong(4, entity.getCaloriesConsumed());
        statement.bindDouble(5, entity.getQuantityConsumed());
        statement.bindString(6, entity.getUnit());
        statement.bindLong(7, entity.getTimestamp());
        statement.bindLong(8, entity.getUserId());
      }
    };
    this.__deletionAdapterOfMealLog = new EntityDeletionOrUpdateAdapter<MealLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `meal_logs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealLog entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final MealLog mealLog, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMealLog.insertAndReturnId(mealLog);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final MealLog mealLog, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMealLog.handle(mealLog);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<MealLog>> getLogsForDate(final long startOfDay, final long endOfDay,
      final long userId) {
    final String _sql = "SELECT * FROM meal_logs WHERE timestamp >= ? AND timestamp < ? AND userId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    _argIndex = 3;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"meal_logs"}, false, new Callable<List<MealLog>>() {
      @Override
      @Nullable
      public List<MealLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPantryItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "pantryItemId");
          final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
          final int _cursorIndexOfCaloriesConsumed = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesConsumed");
          final int _cursorIndexOfQuantityConsumed = CursorUtil.getColumnIndexOrThrow(_cursor, "quantityConsumed");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<MealLog> _result = new ArrayList<MealLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPantryItemId;
            _tmpPantryItemId = _cursor.getLong(_cursorIndexOfPantryItemId);
            final String _tmpItemName;
            _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
            final int _tmpCaloriesConsumed;
            _tmpCaloriesConsumed = _cursor.getInt(_cursorIndexOfCaloriesConsumed);
            final double _tmpQuantityConsumed;
            _tmpQuantityConsumed = _cursor.getDouble(_cursorIndexOfQuantityConsumed);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new MealLog(_tmpId,_tmpPantryItemId,_tmpItemName,_tmpCaloriesConsumed,_tmpQuantityConsumed,_tmpUnit,_tmpTimestamp,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Integer> getTotalCaloriesForDate(final long startOfDay, final long endOfDay,
      final long userId) {
    final String _sql = "SELECT COALESCE(SUM(caloriesConsumed), 0) FROM meal_logs WHERE timestamp >= ? AND timestamp < ? AND userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    _argIndex = 3;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"meal_logs"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalCaloriesForDateSync(final long startOfDay, final long endOfDay,
      final long userId, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(SUM(caloriesConsumed), 0) FROM meal_logs WHERE timestamp >= ? AND timestamp < ? AND userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    _argIndex = 3;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLogsForDateRange(final long startDate, final long endDate, final long userId,
      final Continuation<? super List<MealLog>> $completion) {
    final String _sql = "SELECT * FROM meal_logs WHERE timestamp >= ? AND timestamp < ? AND userId = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    _argIndex = 3;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MealLog>>() {
      @Override
      @NonNull
      public List<MealLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPantryItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "pantryItemId");
          final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
          final int _cursorIndexOfCaloriesConsumed = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriesConsumed");
          final int _cursorIndexOfQuantityConsumed = CursorUtil.getColumnIndexOrThrow(_cursor, "quantityConsumed");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<MealLog> _result = new ArrayList<MealLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPantryItemId;
            _tmpPantryItemId = _cursor.getLong(_cursorIndexOfPantryItemId);
            final String _tmpItemName;
            _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
            final int _tmpCaloriesConsumed;
            _tmpCaloriesConsumed = _cursor.getInt(_cursorIndexOfCaloriesConsumed);
            final double _tmpQuantityConsumed;
            _tmpQuantityConsumed = _cursor.getDouble(_cursorIndexOfQuantityConsumed);
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            _item = new MealLog(_tmpId,_tmpPantryItemId,_tmpItemName,_tmpCaloriesConsumed,_tmpQuantityConsumed,_tmpUnit,_tmpTimestamp,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
