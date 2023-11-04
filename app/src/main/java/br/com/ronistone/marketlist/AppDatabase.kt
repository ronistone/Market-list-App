package br.com.ronistone.marketlist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.ronistone.marketlist.dao.MarketDao
import br.com.ronistone.marketlist.dao.ProductDao
import br.com.ronistone.marketlist.dao.ProductInstanceDao
import br.com.ronistone.marketlist.dao.PurchaseDao
import br.com.ronistone.marketlist.dao.PurchaseItemDao
import br.com.ronistone.marketlist.dao.model.Converters
import br.com.ronistone.marketlist.dao.model.Market
import br.com.ronistone.marketlist.dao.model.Product
import br.com.ronistone.marketlist.dao.model.ProductInstance
import br.com.ronistone.marketlist.dao.model.Purchase
import br.com.ronistone.marketlist.dao.model.PurchaseItem
import br.com.ronistone.marketlist.dao.model.User

@Database(entities = [ Purchase::class, PurchaseItem::class, Market::class, User::class, ProductInstance::class, Product::class ], version = 1)
@TypeConverters( value = [ Converters::class ] )
abstract class AppDatabase: RoomDatabase() {

    abstract fun purchaseDao(): PurchaseDao
    abstract fun purchaseItemDao(): PurchaseItemDao
    abstract fun marketDao(): MarketDao
    abstract fun productDao(): ProductDao
    abstract fun productInstanceDao(): ProductInstanceDao

}


class Database private constructor(){
    companion object {
        private var db: AppDatabase? = null

        fun getDatabase(applicationContext: Context): AppDatabase? {
            if (db == null) {
                synchronized(this) {
                    if(db == null) {
                        db = Room.databaseBuilder(
                            applicationContext,
                            AppDatabase::class.java,
                            "marketListDb"
                        ).build()
                    }
                }
            }
            return db
        }
    }
}
