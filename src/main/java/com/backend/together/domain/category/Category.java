package com.backend.together.domain.category;

//import com.backend.domain.mapping.PostCategory;


public enum Category {
    HOBBY, PLAY, EXERCISE, EAT;

    public static Category fromString(String value) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid category value: " + value);
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private  Long id;
//
//    @Column(nullable = false, length = 40)
//    private String categoryName;
//
//////// 잘 모르겠다....category에서 리스트를 만들지 enum으로 할지
////    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
////    private List<Post> postList = new ArrayList<>();
}
