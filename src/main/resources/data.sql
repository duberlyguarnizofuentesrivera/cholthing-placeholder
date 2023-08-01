/*
 * Copyright (c) 2023. Duberly Guarnizo
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 -----------------ONLY FOR TESTING PURPOSES!---------------------
      This file is to be deleted in production.
 */

INSERT INTO public.image (id, created_by, created_date, deleted, modified_by, modified_date, name, path, version)
VALUES (1, 1, '2023-07-31 21:38:09.000000', false, 1, '2023-07-31 21:38:21.000000', 'ropa deportiva', '1.png', 1)
ON CONFLICT DO NOTHING;
INSERT INTO public.image (id, created_by, created_date, deleted, modified_by, modified_date, name, path, version)
VALUES (2, 1, '2023-07-31 22:29:40.000000', false, 1, '2023-07-31 22:29:47.000000', 'conjunto indú', '2.png', 1)
ON CONFLICT DO NOTHING;
