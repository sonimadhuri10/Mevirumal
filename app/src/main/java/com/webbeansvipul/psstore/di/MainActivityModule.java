package com.webbeansvipul.psstore.di;


import com.webbeansvipul.psstore.MainActivity;
import com.webbeansvipul.psstore.ui.apploading.AppLoadingActivity;
import com.webbeansvipul.psstore.ui.apploading.AppLoadingFragment;
import com.webbeansvipul.psstore.ui.basket.BasketListActivity;
import com.webbeansvipul.psstore.ui.basket.BasketListFragment;
import com.webbeansvipul.psstore.ui.blog.detail.BlogDetailActivity;
import com.webbeansvipul.psstore.ui.blog.detail.BlogDetailFragment;
import com.webbeansvipul.psstore.ui.blog.list.BlogListActivity;
import com.webbeansvipul.psstore.ui.blog.list.BlogListFragment;
import com.webbeansvipul.psstore.ui.category.CategoryListActivity;
import com.webbeansvipul.psstore.ui.category.CategoryListFragment;
import com.webbeansvipul.psstore.ui.category.TrendingCategoryFragment;
import com.webbeansvipul.psstore.ui.checkout.CheckoutActivity;
import com.webbeansvipul.psstore.ui.checkout.CheckoutFragment1;
import com.webbeansvipul.psstore.ui.checkout.CheckoutFragment2;
import com.webbeansvipul.psstore.ui.checkout.CheckoutFragment3;
import com.webbeansvipul.psstore.ui.checkout.CheckoutStatusFragment;
import com.webbeansvipul.psstore.ui.collection.CollectionActivity;
import com.webbeansvipul.psstore.ui.collection.CollectionFragment;
import com.webbeansvipul.psstore.ui.collection.productCollectionHeader.ProductCollectionHeaderListFragment;
import com.webbeansvipul.psstore.ui.comment.detail.CommentDetailActivity;
import com.webbeansvipul.psstore.ui.comment.detail.CommentDetailFragment;
import com.webbeansvipul.psstore.ui.comment.list.CommentListActivity;
import com.webbeansvipul.psstore.ui.comment.list.CommentListFragment;
import com.webbeansvipul.psstore.ui.contactus.ContactUsFragment;
import com.webbeansvipul.psstore.ui.forceupdate.ForceUpdateActivity;
import com.webbeansvipul.psstore.ui.forceupdate.ForceUpdateFragment;
import com.webbeansvipul.psstore.ui.gallery.GalleryActivity;
import com.webbeansvipul.psstore.ui.gallery.GalleryFragment;
import com.webbeansvipul.psstore.ui.gallery.detail.GalleryDetailActivity;
import com.webbeansvipul.psstore.ui.gallery.detail.GalleryDetailFragment;
import com.webbeansvipul.psstore.ui.language.LanguageFragment;
import com.webbeansvipul.psstore.ui.privacyandpolicy.PrivacyPolicyActivity;
import com.webbeansvipul.psstore.ui.privacyandpolicy.PrivacyPolicyFragment;
import com.webbeansvipul.psstore.ui.product.MainFragment;
import com.webbeansvipul.psstore.ui.notification.detail.NotificationActivity;
import com.webbeansvipul.psstore.ui.notification.detail.NotificationFragment;
import com.webbeansvipul.psstore.ui.notification.list.NotificationListActivity;
import com.webbeansvipul.psstore.ui.notification.list.NotificationListFragment;
import com.webbeansvipul.psstore.ui.notification.setting.NotificationSettingFragment;
import com.webbeansvipul.psstore.ui.product.detail.ProductActivity;
import com.webbeansvipul.psstore.ui.product.detail.ProductDetailFragment;
import com.webbeansvipul.psstore.ui.product.favourite.FavouriteListActivity;
import com.webbeansvipul.psstore.ui.product.favourite.FavouriteListFragment;
import com.webbeansvipul.psstore.ui.product.filtering.CategoryFilterFragment;
import com.webbeansvipul.psstore.ui.product.filtering.FilterFragment;
import com.webbeansvipul.psstore.ui.product.filtering.FilteringActivity;
import com.webbeansvipul.psstore.ui.product.history.HistoryFragment;
import com.webbeansvipul.psstore.ui.product.history.UserHistoryListActivity;
import com.webbeansvipul.psstore.ui.product.list.ProductListActivity;
import com.webbeansvipul.psstore.ui.product.list.ProductListFragment;
import com.webbeansvipul.psstore.ui.product.productbycatId.ProductListByCatIdActivity;
import com.webbeansvipul.psstore.ui.product.productbycatId.ProductListByCatIdFragment;
import com.webbeansvipul.psstore.ui.product.search.SearchByCategoryActivity;
import com.webbeansvipul.psstore.ui.product.search.SearchCategoryFragment;
import com.webbeansvipul.psstore.ui.product.search.SearchCityListFragment;
import com.webbeansvipul.psstore.ui.product.search.SearchCountryListFragment;
import com.webbeansvipul.psstore.ui.product.search.SearchFragment;
import com.webbeansvipul.psstore.ui.product.search.SearchSubCategoryFragment;
import com.webbeansvipul.psstore.ui.rating.RatingListActivity;
import com.webbeansvipul.psstore.ui.rating.RatingListFragment;
import com.webbeansvipul.psstore.ui.setting.AppInfoActivity;
import com.webbeansvipul.psstore.ui.setting.AppInfoFragment;
import com.webbeansvipul.psstore.ui.setting.NotificationSettingActivity;
import com.webbeansvipul.psstore.ui.setting.SettingActivity;
import com.webbeansvipul.psstore.ui.setting.SettingFragment;
import com.webbeansvipul.psstore.ui.shop.ShopProfileFragment;
import com.webbeansvipul.psstore.ui.stripe.StripeActivity;
import com.webbeansvipul.psstore.ui.stripe.StripeFragment;
import com.webbeansvipul.psstore.ui.terms.TermsAndConditionsActivity;
import com.webbeansvipul.psstore.ui.terms.TermsAndConditionsFragment;
import com.webbeansvipul.psstore.ui.transaction.detail.TransactionActivity;
import com.webbeansvipul.psstore.ui.transaction.detail.TransactionFragment;
import com.webbeansvipul.psstore.ui.transaction.list.TransactionListActivity;
import com.webbeansvipul.psstore.ui.transaction.list.TransactionListFragment;
import com.webbeansvipul.psstore.ui.user.PasswordChangeActivity;
import com.webbeansvipul.psstore.ui.user.PasswordChangeFragment;
import com.webbeansvipul.psstore.ui.user.ProfileEditActivity;
import com.webbeansvipul.psstore.ui.user.ProfileEditFragment;
import com.webbeansvipul.psstore.ui.user.ProfileFragment;
import com.webbeansvipul.psstore.ui.user.UserForgotPasswordActivity;
import com.webbeansvipul.psstore.ui.user.UserForgotPasswordFragment;
import com.webbeansvipul.psstore.ui.user.UserLoginActivity;
import com.webbeansvipul.psstore.ui.user.UserLoginFragment;
import com.webbeansvipul.psstore.ui.user.UserRegisterActivity;
import com.webbeansvipul.psstore.ui.user.UserRegisterFragment;
import com.webbeansvipul.psstore.ui.user.phonelogin.PhoneLoginActivity;
import com.webbeansvipul.psstore.ui.user.phonelogin.PhoneLoginFragment;
import com.webbeansvipul.psstore.ui.user.verifyemail.VerifyEmailActivity;
import com.webbeansvipul.psstore.ui.user.verifyemail.VerifyEmailFragment;
import com.webbeansvipul.psstore.ui.user.verifyphone.VerifyMobileActivity;
import com.webbeansvipul.psstore.ui.user.verifyphone.VerifyMobileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = TransactionModule.class)
    abstract TransactionListActivity contributeTransactionActivity();

    @ContributesAndroidInjector(modules = FavouriteListModule.class)
    abstract FavouriteListActivity contributeFavouriteListActivity();

    @ContributesAndroidInjector(modules = UserHistoryModule.class)
    abstract UserHistoryListActivity contributeUserHistoryListActivity();

    @ContributesAndroidInjector(modules = UserRegisterModule.class)
    abstract UserRegisterActivity contributeUserRegisterActivity();

    @ContributesAndroidInjector(modules = UserForgotPasswordModule.class)
    abstract UserForgotPasswordActivity contributeUserForgotPasswordActivity();

    @ContributesAndroidInjector(modules = UserLoginModule.class)
    abstract UserLoginActivity contributeUserLoginActivity();

    @ContributesAndroidInjector(modules = PasswordChangeModule.class)
    abstract PasswordChangeActivity contributePasswordChangeActivity();

    @ContributesAndroidInjector(modules = ProductListByCatIdModule.class)
    abstract ProductListByCatIdActivity productListByCatIdActivity();

    @ContributesAndroidInjector(modules = FilteringModule.class)
    abstract FilteringActivity filteringActivity();

    @ContributesAndroidInjector(modules = CommentDetailModule.class)
    abstract CommentDetailActivity commentDetailActivity();

    @ContributesAndroidInjector(modules = DiscountDetailModule.class)
    abstract ProductActivity discountDetailActivity();

    @ContributesAndroidInjector(modules = NotificationModule.class)
    abstract NotificationListActivity notificationActivity();

    @ContributesAndroidInjector(modules = HomeFilteringActivityModule.class)
    abstract ProductListActivity contributehomeFilteringActivity();

    @ContributesAndroidInjector(modules = NotificationDetailModule.class)
    abstract NotificationActivity notificationDetailActivity();

    @ContributesAndroidInjector(modules = TransactionDetailModule.class)
    abstract TransactionActivity transactionDetailActivity();

    @ContributesAndroidInjector(modules = CheckoutActivityModule.class)
    abstract CheckoutActivity checkoutActivity();

    @ContributesAndroidInjector(modules = CommentListActivityModule.class)
    abstract CommentListActivity commentListActivity();

    @ContributesAndroidInjector(modules = BasketlistActivityModule.class)
    abstract BasketListActivity basketListActivity();

    @ContributesAndroidInjector(modules = GalleryDetailActivityModule.class)
    abstract GalleryDetailActivity galleryDetailActivity();

    @ContributesAndroidInjector(modules = GalleryActivityModule.class)
    abstract GalleryActivity galleryActivity();

    @ContributesAndroidInjector(modules = SearchByCategoryActivityModule.class)
    abstract SearchByCategoryActivity searchByCategoryActivity();

    @ContributesAndroidInjector(modules = TermsAndConditionsModule.class)
    abstract TermsAndConditionsActivity termsAndConditionsActivity();

    @ContributesAndroidInjector(modules = EditSettingModule.class)
    abstract SettingActivity editSettingActivity();

    @ContributesAndroidInjector(modules = LanguageChangeModule.class)
    abstract NotificationSettingActivity languageChangeActivity();

    @ContributesAndroidInjector(modules = ProfileEditModule.class)
    abstract ProfileEditActivity contributeProfileEditActivity();

    @ContributesAndroidInjector(modules = AppInfoModule.class)
    abstract AppInfoActivity AppInfoActivity();

    @ContributesAndroidInjector(modules = CategoryListActivityAppInfoModule.class)
    abstract CategoryListActivity categoryListActivity();

    @ContributesAndroidInjector(modules = RatingListActivityModule.class)
    abstract RatingListActivity ratingListActivity();

    @ContributesAndroidInjector(modules = CollectionModule.class)
    abstract CollectionActivity collectionActivity();

    @ContributesAndroidInjector(modules = StripeModule.class)
    abstract StripeActivity stripeActivity();

    @ContributesAndroidInjector(modules = BlogListActivityModule.class)
    abstract BlogListActivity BlogListActivity();

    @ContributesAndroidInjector(modules = BlogDetailModule.class)
    abstract BlogDetailActivity blogDetailActivity();

    @ContributesAndroidInjector(modules = forceUpdateModule.class)
    abstract ForceUpdateActivity forceUpdateActivity();

    @ContributesAndroidInjector(modules = appLoadingModule.class)
    abstract AppLoadingActivity appLoadingActivity();

    @ContributesAndroidInjector(modules = VerifyEmailModule.class)
    abstract VerifyEmailActivity contributeVerifyEmailActivity();

    @ContributesAndroidInjector(modules = PrivacyPolicyActivityModule.class)
    abstract PrivacyPolicyActivity contributePrivacyPolicyActivity();

    @ContributesAndroidInjector(modules = PhoneLoginActivityModule.class)
    abstract PhoneLoginActivity contributePhoneLoginActivity();

    @ContributesAndroidInjector(modules = VerifyMobileModule.class)
    abstract VerifyMobileActivity contributeVerifyMobileActivity();
}

@Module
abstract class CheckoutActivityModule {

    @ContributesAndroidInjector
    abstract CheckoutFragment1 checkoutFragment1();

    @ContributesAndroidInjector
    abstract LanguageFragment languageFragment();

    @ContributesAndroidInjector
    abstract CheckoutFragment2 checkoutFragment2();

    @ContributesAndroidInjector
    abstract CheckoutFragment3 checkoutFragment3();

    @ContributesAndroidInjector
    abstract CheckoutStatusFragment checkoutStatusFragment();
}

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    abstract ProductListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract MainFragment contributeSelectedShopFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract ProductCollectionHeaderListFragment contributeProductCollectionHeaderListFragment();

    @ContributesAndroidInjector
    abstract ContactUsFragment contributeContactUsFragment();

    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();

    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();

    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();

    @ContributesAndroidInjector
    abstract NotificationSettingFragment contributeNotificationSettingFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract LanguageFragment contributeLanguageFragment();

    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteListFragment();

    @ContributesAndroidInjector
    abstract TransactionListFragment contributTransactionListFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributEditSettingFragment();

    @ContributesAndroidInjector
    abstract HistoryFragment historyFragment();

    @ContributesAndroidInjector
    abstract ShopProfileFragment contributeAboutUsFragmentFragment();

    @ContributesAndroidInjector
    abstract BasketListFragment basketFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract NotificationListFragment contributeNotificationFragment();

    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();

    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();

    @ContributesAndroidInjector
    abstract PrivacyPolicyFragment contributePrivacyPolicyFragment();

    @ContributesAndroidInjector
    abstract PhoneLoginFragment contributePhoneLoginFragment();

    @ContributesAndroidInjector
    abstract VerifyMobileFragment contributeVerifyMobileFragment();
}


@Module
abstract class ProfileEditModule {
    @ContributesAndroidInjector
    abstract ProfileEditFragment contributeProfileEditFragment();
}

@Module
abstract class TransactionModule {
    @ContributesAndroidInjector
    abstract TransactionListFragment contributeTransactionListFragment();
}


@Module
abstract class FavouriteListModule {
    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteFragment();
}

@Module
abstract class UserRegisterModule {
    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();
}

@Module
abstract class UserForgotPasswordModule {
    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();
}

@Module
abstract class UserLoginModule {
    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();
}

@Module
abstract class PasswordChangeModule {
    @ContributesAndroidInjector
    abstract PasswordChangeFragment contributePasswordChangeFragment();
}

@Module
abstract class CommentDetailModule {
    @ContributesAndroidInjector
    abstract CommentDetailFragment commentDetailFragment();
}

@Module
abstract class DiscountDetailModule {
    @ContributesAndroidInjector
    abstract ProductDetailFragment discountDetailFragment();
}

@Module
abstract class NotificationModule {
    @ContributesAndroidInjector
    abstract NotificationListFragment notificationFragment();


}


@Module
abstract class NotificationDetailModule {
    @ContributesAndroidInjector
    abstract NotificationFragment notificationDetailFragment();
}

@Module
abstract class TransactionDetailModule {
    @ContributesAndroidInjector
    abstract TransactionFragment transactionDetailFragment();
}

@Module
abstract class UserHistoryModule {
    @ContributesAndroidInjector
    abstract HistoryFragment contributeHistoryFragment();
}

@Module
abstract class AppInfoModule {
    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();
}

@Module
abstract class CategoryListActivityAppInfoModule {
    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract TrendingCategoryFragment contributeTrendingCategoryFragment();
}

@Module
abstract class RatingListActivityModule {
    @ContributesAndroidInjector
    abstract RatingListFragment contributeRatingListFragment();
}

@Module
abstract class TermsAndConditionsModule {
    @ContributesAndroidInjector
    abstract TermsAndConditionsFragment TermsAndConditionsFragment();
}

@Module
abstract class EditSettingModule {
    @ContributesAndroidInjector
    abstract SettingFragment EditSettingFragment();
}

@Module
abstract class LanguageChangeModule {
    @ContributesAndroidInjector
    abstract NotificationSettingFragment notificationSettingFragment();
}

@Module
abstract class EditProfileModule {
    @ContributesAndroidInjector
    abstract ProfileFragment ProfileFragment();
}

@Module
abstract class ProductListByCatIdModule {
    @ContributesAndroidInjector
    abstract ProductListByCatIdFragment contributeProductListByCatIdFragment();

}

@Module
abstract class FilteringModule {

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract FilterFragment contributeSpecialFilteringFragment();
}

@Module
abstract class HomeFilteringActivityModule {
    @ContributesAndroidInjector
    abstract ProductListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract CollectionFragment contributeCollectionFragment();
}

@Module
abstract class CommentListActivityModule {
    @ContributesAndroidInjector
    abstract CommentListFragment contributeCommentListFragment();
}

@Module
abstract class BasketlistActivityModule {
    @ContributesAndroidInjector
    abstract BasketListFragment contributeBasketListFragment();
}

@Module
abstract class GalleryDetailActivityModule {
    @ContributesAndroidInjector
    abstract GalleryDetailFragment contributeGalleryDetailFragment();
}

@Module
abstract class GalleryActivityModule {
    @ContributesAndroidInjector
    abstract GalleryFragment contributeGalleryFragment();
}

@Module
abstract class SearchByCategoryActivityModule {

    @ContributesAndroidInjector
    abstract SearchCategoryFragment contributeSearchCategoryFragment();

    @ContributesAndroidInjector
    abstract SearchSubCategoryFragment contributeSearchSubCategoryFragment();

    @ContributesAndroidInjector
    abstract SearchCityListFragment contributeSearchCityListFragment();

    @ContributesAndroidInjector
    abstract SearchCountryListFragment contributeSearchCountryListFragment();

}

@Module
abstract class CollectionModule {

    @ContributesAndroidInjector
    abstract CollectionFragment contributeCollectionFragment();

}

@Module
abstract class StripeModule {

    @ContributesAndroidInjector
    abstract StripeFragment contributeStripeFragment();

}

@Module
abstract class BlogListActivityModule {

    @ContributesAndroidInjector
    abstract BlogListFragment contributeBlogListFragment();

}

@Module
abstract class BlogDetailModule {

    @ContributesAndroidInjector
    abstract BlogDetailFragment contributeBlogDetailFragment();
}

@Module
abstract class forceUpdateModule {

    @ContributesAndroidInjector
    abstract ForceUpdateFragment contributeForceUpdateFragment();
}

@Module
abstract class appLoadingModule {

    @ContributesAndroidInjector
    abstract AppLoadingFragment contributeAppLoadingFragment();
}

@Module
abstract class VerifyEmailModule {
    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();
}

@Module
abstract class PrivacyPolicyActivityModule {
    @ContributesAndroidInjector
    abstract PrivacyPolicyFragment contributePrivacyPolicyFragment();
}

@Module
abstract class PhoneLoginActivityModule {
    @ContributesAndroidInjector
    abstract PhoneLoginFragment cameraPhoneLoginFragment();
}

@Module
abstract class VerifyMobileModule {
    @ContributesAndroidInjector
    abstract VerifyMobileFragment contributeVerifyMobileFragment();
}

