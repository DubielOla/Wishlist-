package com.raywenderlich.android.wishlist.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raywenderlich.android.wishlist.Wishlist
import com.raywenderlich.android.wishlist.persistance.WishlistDao
import com.raywenderlich.android.wishlist.persistance.WishlistDatabase
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify


@RunWith(AndroidJUnit4::class)
class WishlistDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wishlistDatabase: WishlistDatabase
    private lateinit var wishlistDao: WishlistDao

    @Before
    fun initDb() {
        // 1
        wishlistDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WishlistDatabase::class.java).build()
        // 2
        wishlistDao = wishlistDatabase.wishlistDao()
    }

    @After
    fun closeDb() {
        wishlistDatabase.close()
    }
    @Test
    fun getAllReturnsEmptyList() {
        val testObserver: Observer<List<Wishlist>> = mock()
        wishlistDao.getAll().observeForever(testObserver)
        verify(testObserver).onChanged(emptyList())
    }

    @Test
    fun saveWishlistsSavesData() {
        // 1
        val wishlist1 = Wishlist("Victoria", listOf(), 1)
        val wishlist2 = Wishlist("Tyler", listOf(), 2)
        wishlistDao.save(wishlist1, wishlist2)

        // 2
        val testObserver: Observer<List<Wishlist>> = mock()
        wishlistDao.getAll().observeForever(testObserver)

        // 3
        val listClass =
            ArrayList::class.java as Class<ArrayList<Wishlist>>
        val argumentCaptor = ArgumentCaptor.forClass(listClass)
        // 4
        verify(testObserver).onChanged(argumentCaptor.capture())
        // 5
        assertTrue(argumentCaptor.value.size > 0)
    }


}