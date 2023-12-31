package br.com.ronistone.marketlist.dao.model

import androidx.room.TypeConverter
import br.com.ronistone.marketlist.model.Market
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

        fun purchaseItemWithDependenciesToPurchaseItem(inner: PurchaseItemWithDependencies): PurchaseItem {
            return PurchaseItem(
                id = inner.purchaseItem.id,
                createdAt = inner.purchaseItem.createdAt,
                quantity = inner.purchaseItem.quantity,
                purchased = inner.purchaseItem.purchased,
                product = productDaoToProductModel(inner.product!!)
            )
        }

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
                items = inner.items?.map { purchaseItemToPurchaseItemWithDependencies(it, inner.id) },
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

        fun purchaseItemToPurchaseItemDao(inner: PurchaseItem, purchaseId: Int?): br.com.ronistone.marketlist.dao.model.PurchaseItem {
            return PurchaseItem(
                id = inner.id,
                createdAt = inner.createdAt,
                quantity = inner.quantity,
                purchased = inner.purchased,
                purchaseId = purchaseId,
                productId = inner.product.id!!,
            )
        }

        fun purchaseItemToPurchaseItemWithDependencies(inner: PurchaseItem, purchaseId: Int?): PurchaseItemWithDependencies {
            return PurchaseItemWithDependencies(
                purchaseItem = purchaseItemToPurchaseItemDao(inner, purchaseId),
                product = productModelToProductDao(inner.product),
            )
        }
    }

}
