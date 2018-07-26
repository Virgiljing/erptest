
package com.redsun.bos.ws.impl;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import com.redsun.bos.ws.ObjectFactory;
import com.redsun.bos.ws.Waybilldetail;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "IWayBillWs", targetNamespace = "http://ws.bos.redsun.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IWayBillWs {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<com.redsun.bos.ws.Waybilldetail>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "waybilldetailList", targetNamespace = "http://ws.bos.redsun.com/", className = "com.redsun.bos.ws.WaybilldetailList")
    @ResponseWrapper(localName = "waybilldetailListResponse", targetNamespace = "http://ws.bos.redsun.com/", className = "com.redsun.bos.ws.WaybilldetailListResponse")
    public List<Waybilldetail> waybilldetailList(
        @WebParam(name = "arg0", targetNamespace = "")
        long arg0);

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg4
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.Long
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addWayBill", targetNamespace = "http://ws.bos.redsun.com/", className = "com.redsun.bos.ws.AddWayBill")
    @ResponseWrapper(localName = "addWayBillResponse", targetNamespace = "http://ws.bos.redsun.com/", className = "com.redsun.bos.ws.AddWayBillResponse")
    public Long addWayBill(
        @WebParam(name = "arg0", targetNamespace = "")
        Long arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4);

}
