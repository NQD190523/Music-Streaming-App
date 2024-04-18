package com.project.appealic

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.project.appealic.data.model.Playlist
import com.project.appealic.data.repository.PlayListRepository
import com.project.appealic.ui.viewmodel.PlayListViewModel
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.stubbing.Answer

class PlayListRepositoryTest {
    @Mock
    lateinit var mocklPlayListViewModel: PlayListViewModel

    @Mock
    lateinit var mockTask: Task<QuerySnapshot>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetAllPlayListSuccess() {
        // Thiết lập kết quả thành công cho mockTask
        val playlist = mutableListOf<Playlist>()// Tạo playlist giả định
        // Tạo mock QuerySnapshot
        val mockQuerySnapshot = mock(QuerySnapshot::class.java)
        `when`(mockTask.addOnSuccessListener(any())).thenAnswer { invocation ->
            val listener = invocation.arguments[0] as OnSuccessListener<QuerySnapshot>
            val answer: Answer<Task<QuerySnapshot>> = Answer {
                listener.onSuccess(mockQuerySnapshot)
                mockTask
            }
            answer.answer(invocation)
        }

        // Gọi hàm getAllPlayList() từ mockPlayListRepository
        mocklPlayListViewModel.getAllPlayList()

        // Kiểm tra xem LiveData _playLists đã được cập nhật chưa
        assertNotNull(mocklPlayListViewModel.playLists.value)
    }

    @Test
    fun testGetAllPlayListFailure() {
        // Tạo mock exception
        val mockException = Exception("Fake exception")

        // Thiết lập kết quả thất bại cho mockTask
        `when`(mockTask.addOnFailureListener(any())).thenAnswer { invocation ->
            val listener = invocation.arguments[0] as OnFailureListener
            val answer: Answer<Task<QuerySnapshot>> = Answer {
                listener.onFailure(mockException)
                mockTask
            }
            answer.answer(invocation)
        }

        // Gọi hàm getAllPlayList() từ mockPlayListRepository
        mocklPlayListViewModel.getAllPlayList()

        // Kiểm tra xem có bị lỗi không
        // Đảm bảo rằng exception đã được ghi vào Log
        // Đây chỉ là một số cách thực hiện, bạn có thể tùy chỉnh phụ thuộc vào cách xử lý lỗi của bạn
    }
}