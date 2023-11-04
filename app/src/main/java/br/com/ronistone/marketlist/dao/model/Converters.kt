package br.com.ronistone.marketlist.dao.model

import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import br.com.ronistone.marketlist.dao.PurchaseDao
import br.com.ronistone.marketlist.model.Market
import br.com.ronistone.marketlist.model.ProductInstance
import br.com.ronistone.marketlist.model.Purchase
import br.com.ronistone.marketlist.model.PurchaseItem
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(value: Date?): Long? {
        return value?.time
    }

    companion object {
        fun purchaseWithDependenciesToPurchase(inner: PurchaseWithDependencies): Purchase {
            return Purchase(
                user = userDaoToUser(inner.user),
                market = marketDaoToMarket(inner.market),
                items = inner.items?.map { purchaseItemWithDependenciesToPurchaseItem(it) },
                marketId = inner.market?.id,
                userId = inner.user?.id,
                id = inner.purchase.id,
                createdAt = inner.purchase.createdAt,
                totalSpent = inner.purchase.totalSpent,
                totalExpected = inner.purchase.totalExpected,
            )
        }

        fun userDaoToUser(inner: User?): br.com.ronistone.marketlist.model.User? {
            return inner?.let {
                br.com.ronistone.marketlist.model.User(
                    id = inner.id,
                    email = inner.email,
                    name = inner.name,
                    password = inner.password,
                    createdAt = inner.createdAt,
                    updatedAt = inner.updatedAt,
                )
            }
        }

        fun marketDaoToMarket(inner: br.com.ronistone.marketlist.dao.model.Market?): Market? {
            return inner?.let {
                Market(
                    id = inner.id,
                    name = inner.name,
                    createdAt = inner.createdAt,
                    updatedAt = inner.updatedAt,
                )
            }
        }

        fun productInstanceWithDependenciesToProductInstance(inner: ProductInstanceWithDependencies): ProductInstance {
            return ProductInstance(
                id = inner.productInstance.id,
                product = productDaoToProductModel(inner.product!!),
                market = marketDaoToMarket(inner.market),
                price = inner.productInstance.price,
                createdAt = inner.productInstance.createdAt
            )
        }

        fun productInstanceToProductInstanceWithDependencies(inner: ProductInstance): ProductInstanceWithDependencies {
            return ProductInstanceWithDependencies(
                productInstance = ProductInstance(
                    id = inner.id,
                    price = inner.price,
                    createdAt = inner.createdAt,
                    productId = inner.product.id!!,
                    marketId = inner.market?.id,
                ),
                market = marketToMarketDao(inner.market),
                product = productModelToProductDao(inner.product),
            )
        }

        fun productInstanceModelToProductInstanceDao(inner: ProductInstance): br.com.ronistone.marketlist.dao.model.ProductInstance {
            return ProductInstance(
                id = inner.id,
                price = inner.price,
                createdAt = inner.createdAt,
                productId = inner.product.id!!,
                marketId = inner.market?.id,
            )
        }

        fun purchaseItemWithDependenciesToPurchaseItem(inner: PurchaseItemWithDependencies): PurchaseItem {
            return PurchaseItem(
                id = inner.purchaseItem.id,
                purchase = Purchase(id = inner.purchaseItem.purchaseId),
                productInstance = productInstanceWithDependenciesToProductInstance(inner.productInstance),
                createdAt = inner.purchaseItem.createdAt,
                quantity = inner.purchaseItem.quantity,
                purchased = inner.purchaseItem.purchased
            )
        }

        fun purchaseItemDaoToPurchaseItem(inner: br.com.ronistone.marketlist.dao.model.PurchaseItem): PurchaseItem {
            return inner.let {
                PurchaseItem(
                    id = inner.id,
                    purchase = Purchase(id = inner.id),
                    productInstance = ProductInstance(id = inner.productInstanceId, product = br.com.ronistone.marketlist.model.Product()),
                    createdAt = inner.createdAt,
                    quantity = inner.quantity,
                    purchased = inner.purchased
                )
            }
        }

//        fun productInstanceDaoToProductInstanceModel(inner: br.com.ronistone.marketlist.dao.model.ProductInstance): ProductInstance {
//            return ProductInstance(
//                id = inner.id,
//                product = br.com.ronistone.marketlist.model.Product(id = inner.productId),
//                market = Market(id = inner.marketId),
//                price = null,
//                createdAt = null
//            )
//        }

        fun productDaoToProductModel(inner: Product): br.com.ronistone.marketlist.model.Product {
            return br.com.ronistone.marketlist.model.Product(
                id = inner.id,
                ean = inner.ean,
                name = inner.name,
                unit = inner.unit,
                size = inner.size,
                createdAt = inner.createdAt,
                updatedAt = inner.updatedAt
            )
        }

        fun productModelToProductDao(inner: br.com.ronistone.marketlist.model.Product): Product {
            return Product(
                id = inner.id,
                ean = inner.ean,
                name = inner.name,
                unit = inner.unit,
                size = inner.size,
                createdAt = inner.createdAt,
                updatedAt = inner.updatedAt
            )
        }


        fun purchaseToPurchaseWithDependencies(inner: Purchase): PurchaseWithDependencies {
            return PurchaseWithDependencies(
                user = userToUserDao(inner.user),
                market = marketToMarketDao(inner.market),
                items = inner.items?.map { purchaseItemToPurchaseItemWithDependencies(it) },
                purchase = br.com.ronistone.marketlist.dao.model.Purchase(
                    createdAt = inner.createdAt,
                    id = inner.id,
                    marketId = inner.marketId,
                    userId = inner.userId,
                    totalSpent = inner.totalSpent,
                    totalExpected = inner.totalExpected,
                )

            )
        }

        fun purchaseModelToPurchaseDao(inner: Purchase): br.com.ronistone.marketlist.dao.model.Purchase {
            return br.com.ronistone.marketlist.dao.model.Purchase(
                createdAt = inner.createdAt,
                id = inner.id,
                marketId = inner.marketId,
                userId = inner.userId,
                totalSpent = inner.totalSpent,
                totalExpected = inner.totalExpected,
            )
        }

        fun userToUserDao(inner: br.com.ronistone.marketlist.model.User?): User? {
            return inner?.let {
                User(
                    id = inner.id,
                    email = inner.email,
                    name = inner.name,
                    password = inner.password,
                    createdAt = inner.createdAt,
                    updatedAt = inner.updatedAt,
                )
            }
        }

        fun marketToMarketDao(inner: Market?): br.com.ronistone.marketlist.dao.model.Market? {
            return inner?.let {
                br.com.ronistone.marketlist.dao.model.Market(
                    id = inner.id,
                    name = inner.name,
                    createdAt = inner.createdAt,
                    updatedAt = inner.updatedAt,
                )
            }
        }

        fun purchaseItemToPurchaseItemDao(inner: PurchaseItem): br.com.ronistone.marketlist.dao.model.PurchaseItem {
            return PurchaseItem(
                id = inner.id,
                createdAt = inner.createdAt,
                quantity = inner.quantity,
                purchased = inner.purchased,
                purchaseId = inner.purchase!!.id,
                productInstanceId = inner.productInstance.id
            )
        }

        fun purchaseItemToPurchaseItemWithDependencies(inner: PurchaseItem): PurchaseItemWithDependencies {
            return PurchaseItemWithDependencies(
                purchaseItem = purchaseItemToPurchaseItemDao(inner),
                productInstance = productInstanceToProductInstanceWithDependencies(inner.productInstance)
            )
        }
    }

}
