package com.ketchupzz.francingsfootwearadmin.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzz.francingsfootwearadmin.repository.auth.AuthRepository
import com.ketchupzz.francingsfootwearadmin.repository.auth.AuthRepositoryImpl
import com.ketchupzz.francingsfootwearadmin.repository.customer.CustomerRepository
import com.ketchupzz.francingsfootwearadmin.repository.customer.CustomerRepositoryImpl
import com.ketchupzz.francingsfootwearadmin.repository.inventory.InventoryRepository
import com.ketchupzz.francingsfootwearadmin.repository.inventory.InventoryRepositoryImpl
import com.ketchupzz.francingsfootwearadmin.repository.messages.MessagesRepository
import com.ketchupzz.francingsfootwearadmin.repository.messages.MessagesRepositoryImpl
import com.ketchupzz.francingsfootwearadmin.repository.products.ProductsRepository
import com.ketchupzz.francingsfootwearadmin.repository.products.ProductsRepositoryImpl
import com.ketchupzz.francingsfootwearadmin.repository.transactions.TransactionRepository
import com.ketchupzz.francingsfootwearadmin.repository.transactions.TransactionRepositoryImpl
import com.ketchupzz.francingsfootwearadmin.repository.variations.VariationRepository
import com.ketchupzz.francingsfootwearadmin.repository.variations.VariationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Singleton
    @Provides
    fun providesAuthRepository(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth, storage: FirebaseStorage) : AuthRepository {
        return AuthRepositoryImpl(firestore,firebaseAuth,storage)
    }

    @Singleton
    @Provides
    fun providesInventoryRepository(firestore: FirebaseFirestore, storage: FirebaseStorage) : InventoryRepository {
        return InventoryRepositoryImpl(firestore,storage)
    }


    @Singleton
    @Provides
    fun provideProductRepository(firestore: FirebaseFirestore, storage: FirebaseStorage) : ProductsRepository {
        return ProductsRepositoryImpl(firestore,storage)
    }

    @Singleton
    @Provides
    fun provideVariationRepository(firestore: FirebaseFirestore, storage: FirebaseStorage) : VariationRepository {
        return VariationRepositoryImpl(firestore,storage)
    }

    @Singleton
    @Provides
    fun provideTransactionRepository(firestore: FirebaseFirestore,storage: FirebaseStorage) : TransactionRepository {
        return TransactionRepositoryImpl(firestore,storage)
    }

    @Singleton
    @Provides
    fun provideCustomerRepository(firestore: FirebaseFirestore) : CustomerRepository {
        return CustomerRepositoryImpl(firestore)
    }
    @Singleton
    @Provides
    fun provideMessagesRepository(firestore: FirebaseFirestore) : MessagesRepository {
        return MessagesRepositoryImpl(firestore)
    }
}