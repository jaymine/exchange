/*
 * This file is part of Bitsquare.
 *
 * Bitsquare is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bitsquare is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bitsquare. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.trade;

import io.bitsquare.storage.Storage;

import java.io.File;
import java.io.Serializable;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeList<T> extends ArrayList<T> implements Serializable {
    // That object is saved to disc. We need to take care of changes to not break deserialization.
    private static final long serialVersionUID = 1L;

    transient private static final Logger log = LoggerFactory.getLogger(TradeList.class);

    transient final private Storage<TradeList> storage;
    transient private ObservableList<T> observableList;
    
    // Superclass is ArrayList, which will be persisted
    
    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////////////////////

    public TradeList(File storageDir, String fileName) {
        this.storage = new Storage<>(storageDir);

        TradeList persisted = storage.initAndGetPersisted(this, fileName);
        if (persisted != null) {
            this.addAll(persisted);
        }
        observableList = FXCollections.observableArrayList(this);
    }

    @Override
    public boolean add(T trade) {
        boolean result = super.add(trade);
        observableList.add(trade);
        storage.save();
        return result;
    }

    @Override
    public boolean remove(Object trade) {
        boolean result = super.remove(trade);
        observableList.remove(trade);
        storage.save();
        return result;
    }

    public ObservableList<T> getObservableList() {
        return observableList;
    }

}
