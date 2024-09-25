package com.example.spb_dlsource_into_db.config;

import com.example.spb_dlsource_into_db.entity.DLCustomer;
import org.springframework.batch.item.ItemProcessor;


public class DLCustomerProcessor  implements ItemProcessor<DLCustomer,DLCustomer> {
    @Override
    public DLCustomer process(DLCustomer dlCustomer) throws Exception{
        return dlCustomer;
    }
}
