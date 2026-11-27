package vn.rikkei.cinemabox.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.rikkei.cinemabox.common.DuplicateException;
import vn.rikkei.cinemabox.common.PageResponse;
import vn.rikkei.cinemabox.common.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse create(CreateCustomerRequest req) {
        if (customerRepository.existsByEmail(req.email())) {
            throw new DuplicateException("Email đã tồn tại: " + req.email());
        }

        Customer customer = new Customer();
        customer.setFullName(req.fullName().trim());
        customer.setEmail(req.email().trim());
        customer.setPhone(req.phone() != null ? req.phone().trim() : null);
        customer.setMemberLevel((req.memberLevel() != null && !req.memberLevel().isBlank())
                ? req.memberLevel().trim() : "STANDARD");

        Customer saved = customerRepository.save(customer);
        return CustomerResponse.from(saved);
    }

    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng id=" + id));
        return CustomerResponse.from(customer);
    }

    public PageResponse<CustomerResponse> list(int page, int size) {
        Page<Customer> p = customerRepository.findAll(
                PageRequest.of(page, size, Sort.by("id").ascending()));
        Page<CustomerResponse> dtoPage = p.map(CustomerResponse::from);
        return PageResponse.from(dtoPage);
    }
}
