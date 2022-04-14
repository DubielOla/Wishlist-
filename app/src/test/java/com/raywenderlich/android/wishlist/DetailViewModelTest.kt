package com.raywenderlich.android.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raywenderlich.android.wishlist.persistance.RepositoryImpl
import com.raywenderlich.android.wishlist.persistance.WishlistDao
import com.raywenderlich.android.wishlist.persistance.WishlistDaoImpl
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify


@RunWith(AndroidJUnit4::class)
class DetailViewModelTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val wishlistDao: WishlistDao = Mockito.spy(WishlistDaoImpl())
    private val viewModel = DetailViewModel(RepositoryImpl(wishlistDao))


    @Test
    fun saveNewItemCallsDatabase(){
        viewModel.saveNewItem(Wishlist("Victoria", listOf("RW Android Apprentice Book", "Android phone"), 1), "Smart watch")

        verify(wishlistDao).save(any())
    }
    @Test
    fun saveViewModelSavesData(){
        val wishlist = Wishlist("Aleksandra", listOf("Bike", "Dress"), 1)
        val name = "Real dog"
        viewModel.saveNewItem(wishlist, name)

        val mockObserver = mock<Observer<Wishlist>>()

        wishlistDao.findById(wishlist.id)
            .observeForever(mockObserver)
        verify(mockObserver).onChanged(
            wishlist.copy(wishes = wishlist.wishes + name))
    }
    @Test
    fun getWshlistCallsDatabase(){
        viewModel.getWishlist(1)

        verify(wishlistDao).findById(any())
    }

    @Test
    fun getWishlistReturnsCorrectData(){
        val wishlist = Wishlist("Aleksandra", listOf("Bike", "Dress"), 1)

        wishlistDao.save(wishlist)

        val mockObserver = mock<Observer<Wishlist>>()
        viewModel.getWishlist(1).observeForever(mockObserver)

        verify(mockObserver).onChanged(wishlist)
    }


}