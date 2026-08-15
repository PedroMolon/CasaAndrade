package com.pedromolon.CasaAndrade.service;

import com.pedromolon.CasaAndrade.dto.request.SaleItemRequest;
import com.pedromolon.CasaAndrade.dto.request.SaleRequest;
import com.pedromolon.CasaAndrade.dto.response.SaleResponse;
import com.pedromolon.CasaAndrade.exception.ResourceNotFoundException;
import com.pedromolon.CasaAndrade.mapper.SaleMapper;
import com.pedromolon.CasaAndrade.model.*;
import com.pedromolon.CasaAndrade.repository.ClientRepository;
import com.pedromolon.CasaAndrade.repository.ProductRepository;
import com.pedromolon.CasaAndrade.repository.SaleRepository;
import com.pedromolon.CasaAndrade.repository.UserRepository;
import com.pedromolon.CasaAndrade.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SecurityUtils securityUtils;

    public SaleService(SaleRepository saleRepository, SaleMapper saleMapper, ClientRepository clientRepository, UserRepository userRepository, ProductRepository productRepository, SecurityUtils securityUtils) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with this id"));

        Long sellerId = securityUtils.getCurrentUserId();

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with this id"));

        Sale sale = new Sale();
        sale.setClient(client);
        sale.setUser(seller);

        BigDecimal total = BigDecimal.ZERO;

        for (SaleItemRequest saleItem : request.items()) {
            Product product = productRepository.findById(saleItem.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with this id"));

            if (product.getQuantity() < saleItem.quantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for product '" + product.getName() + "'. Available: "
                        + product.getQuantity() + ", requested: " + saleItem.quantity()
                );
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(saleItem.quantity()));

            SaleItem item = new SaleItem();
            item.setProduct(product);
            item.setQuantity(saleItem.quantity());
            item.setUnitPrice(unitPrice);
            item.setSubtotal(subtotal);

            sale.addItem(item);

            product.setQuantity(product.getQuantity() - saleItem.quantity());
            productRepository.save(product);

            total = total.add(subtotal);
        }

        sale.setTotal(total);
        Sale savedSale = saleRepository.save(sale);
        return saleMapper.toResponse(savedSale);
    }

    @Transactional(readOnly = true)
    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id"));
        return saleMapper.toResponse(sale);
    }

    @Transactional(readOnly = true)
    public Page<SaleResponse> getByClient(Long clientId, Pageable pageable) {
        return saleRepository.findByClientId(clientId, pageable)
                .map(saleMapper::toResponse);
    }

}
