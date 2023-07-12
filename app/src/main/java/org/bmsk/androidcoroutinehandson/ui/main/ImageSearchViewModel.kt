package org.bmsk.androidcoroutinehandson.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.bmsk.androidcoroutinehandson.data.NaverImageSearchRepository
import org.bmsk.androidcoroutinehandson.model.Item

class ImageSearchViewModel : ViewModel() {
    private val repository = NaverImageSearchRepository()
    private val queryFlow = MutableSharedFlow<String>()
    private val favorites = mutableSetOf<Item>()
    private val _favoritesFlow = MutableSharedFlow<List<Item>>(replay = 1)

    /**
     * Flow 는 콜드 스트림이기 때문에 collect 해야 값을 주는 구조입니다.
     * SharedFlow 는 핫 스트림으로 언제든 값을 흘려 보내는 구조입니다.
     * 또한 여러 명이 구독할 수 있는 구조로 되어 있습니다.
     *
     * 메인 엑티비티와 뷰모델끼리 데이터를 공유하기 위해 SharedFlow 를 사용합니다.
     */

    /**
     * 예를 들어 다음 pagingDataFlow 는 사용자로부터 query를 입력 받습니다.
     */
    val pagingDataFlow = queryFlow
        /**
         * 여기서는 쿼리가 변경될 때마다 이미 실행중인 searchImages 호출을 취소하고 새로운 쿼리에 대해 searchImages를 호출합니다.
         * 이렇게 하면 항상 최신의 쿼리 결과만을 사용하고 불필요한 네트워크 요청이나 계산을 줄일 수 있습니다.
         */
        .flatMapLatest {
            searchImages(it)
        }
        /**
         * PagingData는 대량의 데이터를 처리하는 데 도움이 됩니다.
         * cachedIn은 한 번의 데이터 요청 결과를 여러 곳에서 재사용할 수 있도록 해줍니다.
         * 이를 통해 메모리 사용량을 최적화하고 네트워크 요청을 줄일 수 있습니다.
         *
         * * cachedIn() 연산자는 새로운 Flow를 반환하며,
         * 이 Flow의 수집자(collector)가 여러 개 있을 경우 원래의 Flow는 한 번만 수집되고
         * 그 결과가 캐시에 저장되어 다른 수집자들에게 제공됩니다.
         * 이렇게 하면 같은 데이터를 여러 번 요청하거나 불필요하게 로드하는 것을 방지할 수 있습니다.
         *
         * 예를 들어, 사용자가 화면을 회전하여 액티비티나 프래그먼트가 재생성되었을 때,
         * 캐시된 Flow는 이미 로드된 데이터를 재사용할 수 있으므로 네트워크 요청이나 데이터베이스 쿼리를 다시 수행할 필요가 없습니다.
         *
         * 이렇게 cachedIn() 연산자를 사용하면 앱의 성능을 향상시키고 데이터 사용량을 줄일 수 있습니다.
         * 또한, 사용자 경험을 개선하고 앱의 전반적인 효율성을 높이는 데 도움이 됩니다.
         *
         * 단, cachedIn()은 ViewModel의 생명주기 동안만 캐시를 유지하므로, 앱의 프로세스가 종료되거나,
         * ViewModel이 제거되면 캐시도 함께 사라집니다. 따라서 장기적인 데이터 유지에는 적합하지 않습니다.
         * 이런 경우에는 데이터베이스나 디스크 캐시 등의 방법을 고려해야 합니다.
         */
        .cachedIn(viewModelScope)

    val favoritesFlow = _favoritesFlow.asSharedFlow()

    private fun searchImages(query: String): Flow<PagingData<Item>> =
        repository.getImageSearch(query)

    fun handleQuery(query: String) {
        viewModelScope.launch {
            queryFlow.emit(query)
        }
    }

    fun toggle(item: Item) {
        if (favorites.contains(item)) {
            favorites.remove(item)
        } else {
            favorites.add(item)
        }

        viewModelScope.launch {
            _favoritesFlow.emit(favorites.toList())
        }
    }
}