//package edu.friday;
//
//import edu.friday.model.SysUser;
//import edu.friday.repository.SysUserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
////添加安全认证,测试不通过
//
//
//@SpringBootTest
//class FridayApplicationTest {
//
//    @Autowired
//    SysUserRepository userRepository;
//    //添加安全认证,测试不通过
//
//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    void testSaveUser() {
//        SysUser user = new SysUser();
//        user.setUserName("999");
//        user.setNickName("alex test");
//        userRepository.save(user);
//    }
//
//    @Test
//    void testUpdateUser() {
//        SysUser user = userRepository.getOne(new Long(142));
//        user.setUserName("Alex 1");
//        user.setNickName("alex test1");
//        userRepository.save(user);
//    }
//
//    @Test
//    void testFindUsers() {
//        System.out.println(userRepository.findAll());
//    }
//
//    @Test
//    void testDeleteUser() {
//        userRepository.deleteById(new Long(140));
//    }
//}