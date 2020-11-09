package com.traidev.mcfresh.Utility;

import com.traidev.mcfresh.Category.CatShopViewModel;
import com.traidev.mcfresh.Category.Shops.Adapters.ModalOffers;
import com.traidev.mcfresh.Category.Shops.Adapters.RatingViewModal;
import com.traidev.mcfresh.HomeMenus.cart.ex.AddressViewModel;
import com.traidev.mcfresh.HomeMenus.cart.ex.PromoViewModel;
import com.traidev.mcfresh.HomeMenus.delivery.DeliveryModal;
import com.traidev.mcfresh.HomeMenus.home.OffersSlidersModel;
import com.traidev.mcfresh.HomeMenus.notify.NotifyViewModel;
import com.traidev.mcfresh.HomeMenus.orders.ModelOrderHistory;
import com.traidev.mcfresh.HomeMenus.orders.OrderViewModa;
import com.traidev.mcfresh.HomeMenus.wallet.ModelWalletHistory;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Main_Interface {

    //String JSONURL = "https://demonuts.com/Demonuts/JsonTest/Tennis/";

    @POST("showPro.php")
    Call<DefaultResponse> getProfile(@Query("proDetails") String key);

    @POST("showPro.php")
    Call<DefaultResponse> getProfilePic(@Query("proDetailsPic") String key);

    @POST("showPro.php")
    Call<DefaultResponse> getFullProfile(@Query("fullproDetails") String key);

    @POST("promo.php")
    Call<DefaultResponse> deleteAddress(
            @Query("deleteAddress") String id
    );

    @POST("showPro.php")
    Call<DefaultResponse> SendFeedBack(
            @Query("userFeed") String key,
            @Query("msg") String id
    );

    @POST("query.php")
    Call<DefaultResponse> AddtoShopFav(
            @Query("shopId") String key,
            @Query("addFavShopId") String id
    );

    @POST("query.php")
    Call<DefaultResponse> RemovetoShopFav(
            @Query("shopId") String key,
            @Query("removeFavShopId") String id
    );

    @POST("wallet.php")
    Call<DefaultResponse> getBal(@Query("custId") String key);

    @POST("single_shop.php")
    Call<DefaultResponse> getBalBlock(@Query("custIdBlock") String key);


    @POST("promo.php")
    Call<DefaultResponse> getShareDetails(@Query("shareId") String key);

    @POST("promo.php")
    Call<DefaultResponse> getYoutube(@Query("urlYoutube") String key);


 @POST("wallet.php")
    Call<DefaultResponse> topUpWallet(
            @Query("custId") String key,
            @Query("topUpAamount") String amount
    );

    @POST("wallet.php")
    Call<List<ModelWalletHistory>> getWalletHistory(@Query("walletId") String key);

    @POST("wallet.php")
    Call<List<ModelWalletHistory>> getTopupHistory(@Query("topupId") String key);

   //Sliders
    @POST("sliders.php")
    Call<DefaultResponse> getHomeSlides(@Query("homeSlider") String key);

    @POST("sliders.php")
    Call<DefaultResponse> getCatSliders(@Query("CatSlider") String key);


    @POST("orders.php")
    Call<List<ModelOrderHistory>> getOrdersHistory(@Query("custOrders") String key);

    @POST("orders.php")
    Call<DefaultResponse> PaytoShop(
            @Query("custId") String cid,
            @Query("shopIdWallet") String key,
            @Query("payAmountBill") String totam
            );

    //Shop Functions
    @POST("shops.php")
    Call<List<CatShopViewModel>> getFavShops(@Query("allFavoriteShops") String key);


    @POST("shops.php")
    Call<List<CatShopViewModel>> getSearchShops(
            @Query("allSearchKeys") String key,
            @Query("searchType") String type);


    @POST("slider_shops_offers.php")
    Call<List<CatShopViewModel>> getSliderShops(
            @Query("slider_id") String key,
            @Query("sliderType") String type);


    @POST("slider_shops_offers.php")
    Call<List<OffersSlidersModel>> getAllSliders(
            @Query("all_offers_sliders") String key);


    @POST("shops.php")
    Call<List<CatShopViewModel>> getPrimes(
            @Query("primeShops") String type);


    @POST("shops.php")
    Call<List<CatShopViewModel>> getShops(@Query("allShopsGrid") String city,@Query("catName") String cat);


    @POST("shops.php")
    Call<List<CatShopViewModel>> getAllShops(@Query("allShopsRand") String cityt);


    @POST("shops.php")
    Call<List<CatShopViewModel>> getCatShops(@Query("allShopsGridCategory") String cat);


    @POST("shops.php")
    Call<List<CatShopViewModel>> getExtraDiscoutShops(@Query("extraDiscountShops") String city);

    @POST("shops.php")
    Call<DefaultResponse> getSingleShop(@Query("getIdShops") String key,@Query("getUser") String ddd);

    @POST("categories.php")
    Call<List<CatShopViewModel>> getCats(@Query("allUpdatesCat") String city);


    @POST("notifications.php")
    Call<List<NotifyViewModel>> getNotifications(@Query("allNotify") String key);

    @POST("promo.php")
    Call<List<PromoViewModel>> getPromos(@Query("allPromo") String key,@Query("paytype") String pay);

    @POST("promo.php")
    Call<List<AddressViewModel>> getSavedAddress(@Query("allAddress") String key);

    @POST("promo.php")
    Call<DefaultResponse> saveAddress(
            @Query("userId") String key,
            @Query("saveAddress") String add,
            @Query("title") String id
    );


    @POST("membership.php")
    Call<DefaultResponse> addMembership(
            @Query("userId") String key,
            @Query("membership") String add
    );

    @POST("membership.php")
    Call<DefaultResponse> getMemberShip(
            @Query("memberuser") String key
    );


    @POST("membership.php")
    Call<DefaultResponse> addTifinMember(
            @Query("userId") String key,
            @Query("tifniMember") String add,
            @Query("address") String red,
            @Query("mobile") String mobi
    );



    @POST("notifications.php")
    Call<DefaultResponse> clearNotify(@Query("clearNotify") String key);


    @POST("profile.php")
    Call<DefaultResponse> updatePro(
            @Query("name") String mobile,
            @Query("mobile") String pass,
            @Query("email") String gen,
            @Query("address") String intrest,
            @Query("updateDetails") String userid
    );


    @POST("profile.php")
    Call<DefaultResponse> updatePassword(
            @Query("password") String gen,
            @Query("updatePass") String userid
    );


    @Multipart
    @POST("profile.php")
    Call<DefaultResponse>
        uploadProfile(  @Part MultipartBody.Part file,
                 @Part("file") RequestBody title,
                 @Part("name") String mobile,
                 @Part("mobile") String pass,
                 @Part("email") String gen,
                 @Part("address") String intrest,
                 @Part("profileUpdate") String userid
    );


    //Single Shop
    @POST("single_shop.php")
    Call<List<ModalOffers>> getOffers(@Query("fetch_res_products") String key,@Query("shop_id_user") String dd,@Query("searchPro") String ser);


    @POST("single_shop.php")
    Call<List<ModalOffers>> getVegOffers(@Query("veg_id_products") String key,@Query("shop_id_user") String dd);


    @POST("single_shop.php")
    Call<List<ModalOffers>> getCartProducts(@Query("userCartPro") String cart);


    @POST("single_shop.php")
    Call<DefaultResponse> AddtoCart(
            @Query("proId") String key,
            @Query("shopId") String shop,
            @Query("qty") String qty,
            @Query("price") String price,
            @Query("add_offer_user") String id
    );

    @POST("single_shop.php")
    Call<DefaultResponse> updateQty(
            @Query("proId") String key,
            @Query("qty") int qty,
            @Query("update_qty_user") String id
    );


    @POST("single_shop.php")
    Call<DefaultResponse> CancelOrder(
            @Query("cancel_user_order") String key,
            @Query("msg") String qty
    );



    @POST("single_shop.php")
    Call<DefaultResponse> RemovefromCart(
            @Query("proId") String key,
            @Query("remove_cart_user") String id
    );


    @POST("single_shop.php")
    Call<DefaultResponse> OrderNow(
            @Query("orderNowButton") String cid,
            @Query("PayType") String cod,
            @Query("orderID") String or,
            @Query("address") String add,
            @Query("oamount") String amount,
            @Query("omsg") String msg
    );

    @POST("single_shop.php")
    Call<DefaultResponse> OrderNowTifin(
            @Query("orderNowTifin") String cid,
            @Query("PayType") String cod,
            @Query("orderID") String or,
            @Query("totalQty") String qty,
            @Query("orderAm") String an,
            @Query("pro") String pro,
            @Query("address") String tfi

    );


    @POST("single_shop.php")
    Call<DefaultResponse> OrderRepeat(
            @Query("orderRepeatButton") String oid,
            @Query("userid") String uid
    );


    //User Login and Update Account Settings
    @POST("userCrud.php")
    Call<DefaultResponse> loginUser(
            @Query("username") String email,
            @Query("security") String pass,
            @Query("tokenUser") String token

    );

    @POST("userCrud.php")
    Call<DefaultResponse> createUser(
            @Query("name") String name,
            @Query("pass") String pass,
            @Query("mobileOtp") String mobile
    );

    @POST("profile.php")
    Call<DefaultResponse> sendVerifyOTP(
            @Query("userUpdateMobile") String id,
            @Query("mobileOtp") String mobile,
            @Query("otp") String otp
    );

    @POST("userCrud.php")
    Call<DefaultResponse> createUserEmail(
            @Query("name") String name,
            @Query("emailUser") String email,
            @Query("password") String pass,
            @Query("tokenUser") String token
    );

    @POST("userCrud.php")
    Call<DefaultResponse> verifyUser(
            @Query("Vmobile") String mobile,
            @Query("verifyOtp") String otp,
            @Query("password") String pass,
            @Query("userCode") String ucode,
            @Query("tokenUser") String totke

    );

    @POST("userCrud.php")
    Call<DefaultResponse> updateUserVerify(
            @Query("updateFirebase") String mobile,
            @Query("password") String pass,
            @Query("tokenUser") String totke

    );


    @POST("profile.php")
    Call<DefaultResponse> verifyMobile(
            @Query("Vmobile") String mobile,
            @Query("verifyOtpProfile") String otp,
            @Query("userCode") String cod

    );

    @POST("single_shop.php")
    Call<DefaultResponse> getShopBal(@Query("custIdShop") String key);

   @POST("orders.php")
   Call<DefaultResponse> getCartAmount(@Query("userCartTotal") String key);

    @POST("rating.php")
    Call<List<RatingViewModal>> getShopRatings(@Query("shopReviews") String key);

    @POST("rating.php")
    Call<DefaultResponse> getuserReview(@Query("shopId") String key, @Query("UserReview") String user);


    @POST("rating.php")
    Call<DefaultResponse> addReviews(
            @Query("ReviewId") String mobile,
            @Query("review") String pass,
            @Query("rating") Float gen,
            @Query("shopId") String intrest
    );

    @POST("sliders.php")
    Call<DefaultResponse> getHomeOffers(@Query("homeOffers") String key);

    @POST("products.php")
    Call<List<ModalOffers>> getProducts(@Query("storeProducts") String user,@Query("search") String search);

    @POST("products.php")
    Call<List<ModalOffers>> getHomeProducts(@Query("homeProducts") String subcat,@Query("userid") String user);


    @POST("userCrud.php")
    Call<DefaultResponse> forgotPassword(
            @Query("forgotmobile") String email
    );

    @POST("userCrud.php")
    Call<DefaultResponse> forgotVerifyUser(
            @Query("Vmobile") String mobile,
            @Query("ResetPassword") String pass,
            @Query("token") String toke
    );

  @POST("userCrud.php")
  Call<DefaultResponse> resendOTP(
          @Query("mobile") String mobile,
          @Query("resendotp") String otp
  );


 @POST("single_shop.php")
    Call<DefaultResponse> getTifinProducts(@Query("tifinProducts") String type);


    /*Payment Getway Code*/
    @POST("categories.php")
    Call<DefaultResponse> getCatBanner(@Query("homeBanner") String key);


    @POST("orders.php")
    Call<DefaultResponse> getOrderDetails(@Query("orderDetails") String key);

    @POST("orders.php")
    Call<List<OrderViewModa>> getOrderProducts(@Query("ordersProd") String key);



    @Multipart
    @POST("delivery.php")
    Call<DefaultResponse>
    addDeliveryPicture(  @Part MultipartBody.Part file,
                    @Part("bill") RequestBody title,
                    @Part("pick") String mobile,
                         @Part("pickMobile") String mob,
                         @Part("dropMobile") String dd,
                         @Part("orderDesc") String fd,
                    @Part("cat") String gen,
                    @Part("amount") String intrest,
                    @Part("pay") String dr,
                    @Part("drop") String pay,
                    @Part("addDeliveryPicture") String del

    );

    @POST("delivery.php")
    Call<List<DeliveryModal>> getDeliverys(@Query("allDelivery") String key);


    @POST("delivery.php")
    Call<DefaultResponse> addDelivery(
            @Query("pick") String email,
            @Query("pickMobile") String mob,
            @Query("dropMobile") String dd,
            @Query("orderDesc") String fd,
            @Query("cat") String cat,
            @Query("amount") String am,
            @Query("pay") String pa,
            @Query("drop") String drop,
            @Query("addDelivery") String token
    );





}
