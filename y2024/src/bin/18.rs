use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use std::collections::HashSet;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "18";
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(reader: R, bounds: usize, take: usize) -> Result<usize> {
        let mut grid = vec![vec!['.'; bounds]; bounds];
        reader.lines().take(take).for_each(|line| {
            let binding = line.unwrap();
            let (left, right) = binding.split_once(",").unwrap();
            let x = left.parse::<usize>().unwrap();
            let y = right.parse::<usize>().unwrap();
            grid[y][x] = '#';
        });

        let mut mins = vec![vec![bounds * bounds; bounds]; bounds];
        let end = (bounds - 1, bounds - 1);
        let mut work_queue = vec![vec![(0, 0)]];
        while !work_queue.is_empty() {
            let work = work_queue.pop().unwrap();
            let curr = work.last().unwrap();
            let len = work.len();

            if len >= mins[curr.0][curr.1] {
                continue; // abort this path
            } else {
                mins[curr.0][curr.1] = len;
            }
            if *curr == end {
                continue;
            }

            //up
            if curr.0 > 0 && grid[curr.0 - 1][curr.1] != '#' {
                let mut up = work.clone();
                up.push((curr.0 - 1, curr.1));
                work_queue.push(up);
            }
            //down
            if curr.0 < (bounds - 1) && grid[curr.0 + 1][curr.1] != '#' {
                let mut down = work.clone();
                down.push((curr.0 + 1, curr.1));
                work_queue.push(down);
            }
            //left
            if curr.1 > 0 && grid[curr.0][curr.1 - 1] != '#' {
                let mut left = work.clone();
                left.push((curr.0, curr.1 - 1));
                work_queue.push(left);
            }

            //right
            if curr.1 < bounds - 1 && grid[curr.0][curr.1 + 1] != '#' {
                let mut right = work.clone();
                right.push((curr.0, curr.1 + 1));
                work_queue.push(right);
            }
        }

        let answer = mins[bounds - 1][bounds - 1] - 1;
        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(22, part1(BufReader::new(TEST.as_bytes()), 7, 12)?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file, 71, 1024)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    // println!("\n=== Part 2 ===");
    //
    // fn part2<R: BufRead>(reader: R) -> Result<usize> {
    //     Ok(0)
    // }
    //
    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);
    //
    // let input_file = BufReader::new(File::open(INPUT_FILE)?);
    // let result = time_snippet!(part2(input_file)?);
    // println!("Result = {}", result);
    //endregion

    Ok(())
}
