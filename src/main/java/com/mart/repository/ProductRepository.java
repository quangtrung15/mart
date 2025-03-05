package com.mart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mart.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	// Tìm kiếm sản phẩm theo thể loại, khoảng giá, tên, thương hiệu.
	@Query("SELECT p FROM products p WHERE " + "(p.category.id = :category_id OR :category_id IS NULL) AND "
			+ "((p.price >= :minPrice OR :minPrice IS NULL) AND (p.price <= :maxPrice OR :maxPrice IS NULL)) AND "
			+ "(p.name LIKE %:name% OR :name IS NULL) AND" 
			+ "(p.brand = :brand OR :brand IS NULL)")
	List<Product> findByCategoryIdAndPriceRangeAndNameAndBrand(@Param("category_id") Integer category_id,
			@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("name") String name,
			@Param("brand") String brand);

	// Hiển thị sản phẩm theo trạng thái.
	List<Product> findByStatusContainingIgnoreCase(String status); // findByStatusContainingIgnoreCase() để tìm trạng
																	// thái gần đúng (chứa từ khóa và không phân biệt
																	// hoa thường).

}
