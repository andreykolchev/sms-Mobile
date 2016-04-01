package com.sms.api;

/*
 * sms
 * Created by A.Kolchev  29.2.2016
 */

import android.database.sqlite.SQLiteDatabase;

import com.sms.App;
import com.sms.data.exception.ModelException;
import com.sms.data.model.ChangeableEntity;
import com.sms.data.model.Presenter;
import com.sms.data.model.Route;
import com.sms.util.EntityUtils;
import com.sms.util.Log;

import java.util.List;


public class ApiManager {
    public static final String TAG = ApiManager.class.getSimpleName();

    /**
     * Set changed entity state based on changed time
     *
     * @param postedChanges
     * @param local
     * @param <T>
     * @return
     * @throws ModelException
     */
    private static <T extends ChangeableEntity> T changeLocalChangedState(List<T> postedChanges, T local) throws ModelException {
        ChangeableEntity sentEntity = EntityUtils.findEntity(postedChanges, local);
        if (sentEntity != null) {
            if (local.isChanged() && sentEntity.isChanged() && local.getLastChange().longValue() == sentEntity.getLastChange()) {
                local.setChanged(false);
                local.save();
            }
        }
        return (T) sentEntity;
    }

    public static synchronized void processGetUpdateRouts(List<Route> data) throws ApiException {
        Log.d(TAG, "Start process getUpdateRouts.");
        if (data == null) {
            throw new ApiException("getUpdateRouts response is null.");
        }
        SQLiteDatabase db = App.getInstance().getDBHelper().getWritableDatabase();
        db.beginTransaction();
        try {
            for (Route route : data) {
                long idLocal = App.getInstance().getDBHelper().getLocalID(Route.TABLE_NAME, route.getServerId());
                if (idLocal > 0) {
                    // save local states
                    Route local = App.getInstance().getDBHelper().getRoutByID(idLocal);
                    local.save();
                }
                route.save();
            }
            db.setTransactionSuccessful();
            Route.notifyObserver(Route.TABLE_NAME);
        } catch (ModelException e) {
            Log.e(TAG, "Can't process getUpdateRouts.", e);
            throw new ApiException("Can't process getUpdateRouts.", e);
        } finally {
            db.endTransaction();
        }
        Log.d(TAG, "End process getUpdateRouts.");
    }

    public static synchronized void processGetUpdatePresenter(List<Presenter> data) throws ApiException {
        Log.d(TAG, "Start process getUpdatePresenter.");
        if (data == null) {
            throw new ApiException("getUpdatePresenter response is null.");
        }
        SQLiteDatabase db = App.getInstance().getDBHelper().getWritableDatabase();
        db.beginTransaction();
        try {
            for (Presenter presenter : data) {
                long idLocal = App.getInstance().getDBHelper().getLocalID(Presenter.TABLE_NAME, presenter.getServerId());
                if (idLocal > 0) {
                    // save local states
                    Presenter local = App.getInstance().getDBHelper().getPresenterItemByID(idLocal);
                    local.save();
                }
                presenter.save();
            }
            db.setTransactionSuccessful();
            Presenter.notifyObserver(Presenter.TABLE_NAME);
        } catch (ModelException e) {
            Log.e(TAG, "Can't process getUpdatePresenter.", e);
            throw new ApiException("Can't process getUpdatePresenter.", e);
        } finally {
            db.endTransaction();
        }
        Log.d(TAG, "End process getUpdatePresenter.");
    }

}
