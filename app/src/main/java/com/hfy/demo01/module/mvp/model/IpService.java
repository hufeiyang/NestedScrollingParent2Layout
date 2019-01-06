package com.hfy.demo01.module.mvp.model;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 访问网络的接口 IpService
 * Retrofit
 */
public interface IpService {

    /**
     * 请求url
     */
    String REQUEST_URL_GET_IP_INFO = "getIpInfo.php";

    /**
     * 获取IpInfo
     * @param ip ip地址
     * @return 返回 Observable<IpInfo> ，Observable为了支持RxJava。（单纯用Retrofit 就返回 Call<IpInfo>）
     */
    @FormUrlEncoded
    @POST(REQUEST_URL_GET_IP_INFO)
    Observable<IpInfo> getIpInfo(@Field("ip") String ip);
}
