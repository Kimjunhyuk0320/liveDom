        // 창 크기 체크 및 조절
        function checkWindowSize() {
            // 최소 창 크기 설정
            const minWidth = 1000;

            // 현재 창 크기 확인
            const currentWidth = window.innerWidth;

            // 최소 창 크기보다 작으면 창 크기를 조절
            if (currentWidth < minWidth) {
                window.resizeTo(minWidth, window.innerHeight);
            }
        }

        // 창 크기가 변경될 때마다 함수 호출
        $(window).on('resize', checkWindowSize);

        // 초기 로딩 시에도 함수 호출
        $(document).ready(checkWindowSize);